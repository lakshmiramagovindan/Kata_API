package com.booking.stepDefinitions;

import com.booking.pojo.AuthPOJO;
import com.booking.routes.Routes;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

public class AuthenticationPost {
    RequestSpecification req;
    Response res;
    AuthPOJO authPOJO;
    @Given("User writes the authentication payload")
    public void userWritesTheAuthenticationPayload() {
        // Write code here that turns the phrase above into concrete actions
        authPOJO = new AuthPOJO();
        authPOJO.setUsername("admin");
        authPOJO.setPassword("password");
    }

    @When("User make a {string} request on the {string} endpoint")
    public void userMakeARequestOnTheEndpoint(String httpMethod, String endPoint) {
        // Write code here that turns the phrase above into concrete actions
        req = new RequestSpecBuilder()
                .setBaseUri(Routes.baseURI)
                .setContentType(ContentType.JSON)
                .build();
        if (httpMethod.equalsIgnoreCase("post")) {
            res = given()
                    .spec(req)
                    .body(authPOJO)
                    .when()
                    .post(endPoint);
        } else {
            throw new IllegalStateException("Unexpected value: " + httpMethod.toLowerCase());
        }
    }

    @Then("API call should be successful with status code {int}")
    public void apiCallShouldBeSuccessfulWithStatusCode(int statusCode) {
        // Write code here that turns the phrase above into concrete actions
        //JsonPath json = res.jsonPath();
        Assert.assertEquals(res.getStatusCode(), statusCode);
    }
}
