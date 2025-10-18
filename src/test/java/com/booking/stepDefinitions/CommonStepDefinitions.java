package com.booking.stepDefinitions;

import com.booking.routes.Routes;
import com.booking.utils.Utils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import java.util.List;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CommonStepDefinitions extends Utils {
    JsonPath json;

    @When("User makes a {string} request on the {string} endpoint")
    public void userMakesARequestOnTheEndpoint(String httpMethod, String endPointKey) {
        List<String> supportedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
        String method = httpMethod.toUpperCase();
        String endPoint = Routes.getEndpoint(endPointKey);
        if (!supportedMethods.contains(method) || endPoint.isEmpty()) {
            throw new IllegalArgumentException(String.format("Unsupported HTTP method %s or endPointKey %s", httpMethod, endPointKey));
        }
        res = req.when().request(method, endPoint);
    }

    @Then("The API status code should be {string}")
    public void theAPIStatusCodeShouldBe(String statusCode) {
        Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
    }

    @And("{string} in response body should not be empty")
    public void inResponseBodyShouldNotBeEmpty(String responseKey) {
        json = res.jsonPath();
        Assert.assertNotNull(json.get(responseKey));
    }

    @Then("Response should match the {string} schema")
    public void shouldMatchTheSchema(String schemaFileName) {
            res.then()
                    .assertThat()
                    .body(matchesJsonSchemaInClasspath("config/"+schemaFileName+".json"));
    }
}