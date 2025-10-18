package com.booking.stepDefinitions;

import com.booking.testData.Payload;
import com.booking.utils.Utils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import java.util.List;
import static io.restassured.RestAssured.given;

public class CommonStepDefinitions extends Utils {
    RequestSpecification req;
    Response res;
    Payload p = new Payload();

    @Given("User writes the authentication payload with {string} and {string}")
    public void userWritesTheAuthenticationPayload(String username, String password) {
        req = given()
                .spec(requestSpecification())
                .body(p.authPayload(username, password));
    }

    @When("User makes a {string} request on the {string} endpoint")
    public void userMakesARequestOnTheEndpoint(String httpMethod, String endPoint) {
        List<String> supportedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
        String method = httpMethod.toUpperCase();

        if (!supportedMethods.contains(method)) {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        res = req.when().request(method, endPoint);
    }

    @Then("The API status code should be {string}")
    public void theAPIStatusCodeShouldBe(String statusCode) {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(res.getStatusCode(), Integer.parseInt(statusCode));
    }
}