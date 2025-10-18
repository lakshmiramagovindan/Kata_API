package com.booking.stepDefinitions;

import com.booking.testData.Payload;
import com.booking.utils.Utils;
import io.cucumber.java.en.Given;
import static io.restassured.RestAssured.given;

public class AuthenticationStepDefinitions extends Utils {
    Payload p = new Payload();
    @Given("User writes the authentication payload with {string} and {string}")
    public void userWritesTheAuthenticationPayload(String username, String password) {
        req = given()
                .spec(requestSpecification())
                .body(p.authPayload(username, password));
    }
}
