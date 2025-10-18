package com.booking.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import static com.booking.utils.Utils.*;
import static io.restassured.RestAssured.given;

public class Hooks {

    @Before
    public void setUp() {
        req = given()
                .spec(baseRequestSpecification());
    }

    @After
    public void tearDown() {
        req = null;
        System.out.println("Test scenario finished. Resources cleaned up.");
    }
}