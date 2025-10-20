package com.booking.hooks;

import com.booking.context.ScenarioContextHolder;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import com.booking.utils.Utils;
import io.restassured.RestAssured;

public class Hooks {
    /**
     * Setup method executed before each test scenario.
     * <p>
     * Initializes the REST Assured request specification using a predefined
     * base configuration from {@code baseRequestSpecification()}.
     */
    @Before
    public void setUp() {
        ScenarioContextHolder.getContext()
                .setReq(RestAssured.given()
                .spec(Utils.baseRequestSpecification()));
    }
    /**
     * Teardown method executed after each test scenario.
     * Cleans up by resetting the request object and logs that the scenario has completed.
     */
    @After
    public void tearDown() {
        ScenarioContextHolder.clear();
        System.out.println("Test scenario finished. Resources cleaned up.");
    }
}