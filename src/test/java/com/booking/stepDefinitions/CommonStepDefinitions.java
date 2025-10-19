package com.booking.stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import java.io.IOException;

import static com.booking.utils.Utils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class CommonStepDefinitions {
    JsonPath json;

    @When("User makes a {string} action to the {string}")
    public void userMakesAActionToTheEndpoint(String httpMethod, String endPointKey) {
        String method = validHttpMethodCheck(httpMethod);
        String endPoint = validEndPointCheck(endPointKey);
        res = req
                .when()
                .request(method, endPoint);

    }
    @When("User makes a {string} action to the {string} via {string}")
    public void userMakesAActionToTheEndpointViaAuthentication(String httpMethod, String endPointKey, String configKey) throws IOException {
        String method = validHttpMethodCheck(httpMethod);
        String endPoint = validEndPointCheck(endPointKey);
        String configValue = requestWithOrWithoutAuthentication(configKey);
        res = req
                .header("Cookie", "token=" + configValue)
                .auth().oauth2(configValue)
                .when()
                .request(method, endPoint);
    }

    @Then("The system should respond with status {string}")
    public void theSystemShouldRespondWithStatus(String statusCode) {
        Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
    }

    @Then("The system should respond with description {string}")
    public void theSystemShouldRespondWithDescription(String statusLine) {
        assertThat(res.getStatusLine(), containsString(statusLine));
    }

    @And("{string} in response should not be empty")
    public void inResponseShouldNotBeEmpty(String responseKey) {
        json = res.jsonPath();
        Assert.assertNotNull(json.get(responseKey));
    }

    @And("{string} in response body should be {string}")
    public void inResponseBodyShouldBe(String key, String expectedRawValue) {
        JsonPath json = res.jsonPath();
        Object responseValue = json.get(key);
        Assert.assertNotNull(responseValue);
        String expectedValue = processDate(sanitizeDateValue(expectedRawValue));
        String actualAsString = convertActualValue(responseValue);

        Assert.assertEquals(actualAsString.replace("[\\[\\]]", " ").trim(), expectedValue);
    }


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
}