package com.booking.stepDefinitions;

import com.booking.testData.Payload;
import static com.booking.utils.Utils.*;
import io.cucumber.java.en.Given;

public class AuthenticationStepDefinitions {

    private final Payload payload = new Payload();

    @Given("User logs in with {string} and {string}")
    public void userLogsInWith(String username, String password) {
        req = req
                .body(payload.authPayload(username, password));
    }
}