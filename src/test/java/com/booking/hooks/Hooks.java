package com.booking.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import static com.booking.utils.Utils.*;
import static io.restassured.RestAssured.given;

public class Hooks {
    /**
     * Setup method executed before each test scenario.
     * <p>
     * Initializes the REST Assured request specification using a predefined
     * base configuration from {@code baseRequestSpecification()}.
     */
    @Before
    public void setUp() {
        req = given()
                .spec(baseRequestSpecification());
    }
    /**
     * Teardown method executed after each test scenario.
     * Cleans up by resetting the request object and logs that the scenario has completed.
     */
    @After
    public void tearDown() {
        req = null;
        System.out.println("Test scenario finished. Resources cleaned up.");
    }
}