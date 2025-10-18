package com.booking.stepDefinitions;

import com.booking.testData.Payload;
import com.booking.utils.Utils;
import io.cucumber.java.en.Given;
import static io.restassured.RestAssured.given;

public class AuthenticationStepDefinitions extends Utils {
    Payload p = new Payload();
    @Given("User logs in with {string} and {string}")
    public void userLogsInWith(String username, String password) {
        req = given()
                .spec(requestSpecification())
                .body(p.authPayload(username, password));
    }
}
