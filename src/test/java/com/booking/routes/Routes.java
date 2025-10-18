package com.booking.routes;

import java.util.Map;

public class Routes {
    private static final Map<String, String> apiRoutes = Map.of(
            "baseURI", "https://automationintesting.online/api",
            "postAuthEndpoint", "/auth/login",
            "postAuthEndpointInvalid", "/auto/login",
            "createBookingEndpoint", "/booking",
            "updateDeleteBookingEndpoint", "/booking/{id}",
            "getHealthEndpoint", "/booking/actuator/health"
    );

    public static String getEndpoint(String endPointKey) {
        return apiRoutes.getOrDefault(endPointKey, "");
    }
}
