package com.booking.stepDefinitions;

import com.booking.testData.Payload;
import static com.booking.utils.Utils.*;
import io.cucumber.java.en.Given;

/**
 * Step definitions for authentication-related Cucumber steps.
 */
public class AuthenticationStepDefinitions {

    /** Payload utility for building request bodies. */
    private final Payload payload = new Payload();
    /**
     * Cucumber step: Logs in the user with the given username and password.
     * <p>
     * Sets the request body using the authentication payload.
     *
     * @param username the username for login
     * @param password the password for login
     */
    @Given("User logs in with {string} and {string}")
    public void userLogsInWith(String username, String password) {
        req = req
                .body(payload.authPayload(username, password));
    }
}