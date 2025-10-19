package com.booking.stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import java.io.IOException;
import static com.booking.utils.Utils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Step definitions for common API actions and validations.
 */
public class CommonStepDefinitions {
    /** Used to extract values from JSON responses. */
    JsonPath json;

    /**
     * Sends an HTTP request to the specified endpoint using the given method.
     *
     * @param httpMethod   HTTP method (e.g., GET, POST, PUT, DELETE)
     * @param endPointKey  Key representing the API endpoint
     */
    @When("User makes a {string} action to the {string}")
    public void userMakesAActionToTheEndpoint(String httpMethod, String endPointKey) {
        String method = validHttpMethodCheck(httpMethod);
        String endPoint = validEndPointCheck(endPointKey);
        res = req
                .when()
                .request(method, endPoint);

    }
    /**
     * Sends an authenticated HTTP request to the specified endpoint.
     *
     * @param httpMethod   HTTP method
     * @param endPointKey  Endpoint key
     * @param configKey    Key for retrieving the token from config
     */
    @When("User makes a {string} action to the {string} via {string}")
    public void userMakesAActionToTheEndpointViaAuthentication(String httpMethod, String endPointKey, String configKey) throws IOException {
        String method = validHttpMethodCheck(httpMethod);
        String endPoint = validEndPointCheck(endPointKey);
        String configValue = requestWithOrWithoutAuthentication(configKey);
        res = req
                .auth().oauth2(configValue)
                .when()
                .request(method, endPoint);
    }

    /**
     * Verifies that the response status code matches the expected value.
     *
     * @param statusCode expected status code as a string
     */
    @Then("The system should respond with status {string}")
    public void theSystemShouldRespondWithStatus(String statusCode) {
        Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
    }

    /**
     * Verifies that the response status line contains the expected text.
     *
     * @param statusLine expected text in the status line
     */
    @Then("The system should respond with description {string}")
    public void theSystemShouldRespondWithDescription(String statusLine) {
        assertThat(res.getStatusLine(), containsString(statusLine));
    }

    /**
     * Verifies that a specific field in the response is not empty.
     *
     * @param responseKey JSON key in the response body
     */
    @And("{string} in response should not be empty")
    public void inResponseShouldNotBeEmpty(String responseKey) {
        json = res.jsonPath();
        Assert.assertNotNull(json.get(responseKey));
    }

    /**
     * Validates that a field in the response body matches the expected value.
     * Also processes the expected value for date formatting.
     *
     * @param key              JSON key to check
     * @param expectedRawValue expected value before formatting
     */
    @And("{string} in response body should be {string}")
    public void inResponseBodyShouldBe(String key, String expectedRawValue) {
        JsonPath json = res.jsonPath();
        Object responseValue = json.get(key);
        Assert.assertNotNull(responseValue);
        String expectedValue = processDate(sanitizeDateValue(expectedRawValue));
        String actualAsString = convertActualValue(responseValue);

        Assert.assertEquals(actualAsString.replace("[\\[\\]]", " ").trim(), expectedValue);
    }

    /**
     * Validates the response body against a JSON schema.
     *
     * @param schemaFileName the name of the schema file located in the config folder
     */
    @Then("Response should match the {string} schema")
    public void shouldMatchTheSchema(String schemaFileName) {
        if(schemaFileName != null && !schemaFileName.trim().isEmpty()) {
            res.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath("config/"+schemaFileName+".json"));
        } else {
            res.then();
        }
    }

    /**
     * Stores a value from the response body into the config file.
     *
     * @param responseKey JSON key to extract from the response
     * @param configKey   key to save the extracted value under in the config file
     */
    @And("Store {string} from response as {string} in config file")
    public void storeFromResponseAsInConfigFile(String responseKey, String configKey) throws IOException {
        json = res.jsonPath();
        Object rawValue = json.get(responseKey);
        String configValue;
        if (rawValue instanceof Integer) {
            configValue = String.valueOf(rawValue);
        } else if (rawValue instanceof String) {
            configValue = (String) rawValue;
        } else {
            return;
        }
        writePropertyFile(configKey, configValue);
    }

    /**
     * Prepares the request with a path parameter and login token as a cookie.
     *
     * @param pathParam    the key for the path parameter value from config
     * @param cookieToken  the key for the token to be added in the Cookie header
     */
    @Given("User sends basic information {string} and the login token {string}")
    public void userSendsBasicInformationAndTheLoginToken(String pathParam, String cookieToken) throws IOException {
        String configValue = requestWithOrWithoutAuthentication(cookieToken);
        req = req
                .pathParam("id", readPropertyFile(pathParam))
                .header("Cookie", "token=" + configValue);
    }
}