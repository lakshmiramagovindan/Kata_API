package com.booking.context;

import lombok.Getter;

public class ScenarioContextHolder {
    @Getter
    private static TestContext context = new TestContext();

    public static void clear() {
        context = new TestContext();
    }
}
