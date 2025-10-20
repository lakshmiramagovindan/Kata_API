package com.booking.stepDefinitions;

import com.booking.context.ScenarioContextHolder;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;

import static com.booking.utils.Utils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ResponseStepDefinitions {

    /**
     * Verifies that the response status code matches the expected value.
     *
     * @param statusCode expected status code as a string
     */
    @Then("The system should respond with status {string}")
    public void theSystemShouldRespondWithStatus(String statusCode) {
        Response res = ScenarioContextHolder.getContext().getRes();
        Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
    }

    /**
     * Verifies that the response status line contains the expected text.
     *
     * @param statusLine expected text in the status line
     */
    @Then("The system should respond with description {string}")
    public void theSystemShouldRespondWithDescription(String statusLine) {
        Response res = ScenarioContextHolder.getContext().getRes();
        assertThat(res.getStatusLine(), containsString(statusLine));
    }

    /**
     * Verifies that a specific field in the response is not empty.
     *
     * @param responseKey JSON key in the response body
     */
    @And("{string} in response should not be empty")
    public void inResponseShouldNotBeEmpty(String responseKey) {
        Response res = ScenarioContextHolder.getContext().getRes();
        JsonPath json = res.jsonPath();
        Assert.assertNotNull(json.get(responseKey));
    }

    /**
     * Validates that a field in the response body matches the expected value.
     *
     * @param key              JSON key to check
     * @param expectedRawValue expected value before formatting
     */
    @And("{string} in response body should be {string}")
    public void inResponseBodyShouldBe(String key, String expectedRawValue) {
        Response res = ScenarioContextHolder.getContext().getRes();
        JsonPath json = res.jsonPath();
        Object responseValue = json.get(key);

        Assert.assertNotNull(responseValue);
        String expectedValue = processDate(sanitizeDateValue(expectedRawValue));
        String actualAsString = convertActualValue(responseValue);

        Assert.assertEquals(actualAsString.replace("[\\[\\]]", " ").trim(), expectedValue);
    }

    /**
     * Validates multiple response fields and conditions from a DataTable.
     * DataTable columns: field | condition | expected value (optional)
     * Example table:
     * | field               | condition | expected value         |
     * | bookingid           | not empty |                        |
     * | firstname           | equals    | John                   |
     * | depositpaid         | equals    | true                   |
     * | bookingdates.checkin| not empty |                        |
     */
    @Then("the response fields should match:")
    public void validateResponseFields(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String field = row.get("field");
            String condition = row.get("condition").toLowerCase();
            String expectedValue = row.get("expected value");

            if ("equals".equalsIgnoreCase(condition)) {
                inResponseBodyShouldBe(field, expectedValue);
            } else if ("not empty".equalsIgnoreCase(condition)) {
                inResponseShouldNotBeEmpty(field);
            } else {
                throw new IllegalArgumentException(String.format("Unsupported condition: %s", condition));
            }
        }
    }

    /**
     * Validates the response body against a JSON schema.
     *
     * @param schemaFileName the name of the schema file located in the config folder
     */
    @Then("Response should match the {string} schema")
    public void shouldMatchTheSchema(String schemaFileName) {
        Response res = ScenarioContextHolder.getContext().getRes();
        if (schemaFileName != null && !schemaFileName.trim().isEmpty()) {
            res.then().assertThat()
                    .body(matchesJsonSchemaInClasspath("config/" + schemaFileName + ".json"));
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
        Response res = ScenarioContextHolder.getContext().getRes();
        JsonPath json = res.jsonPath();
        Object rawValue = json.get(responseKey);

        if (rawValue != null) {
            String valueToStore = String.valueOf(rawValue);
            writePropertyFile(configKey, valueToStore);
        }
    }
}
