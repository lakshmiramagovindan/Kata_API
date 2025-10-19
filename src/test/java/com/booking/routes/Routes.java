package com.booking.routes;

import java.util.Map;

/**
 * Utility class that stores API route mappings for the booking application.
 * <p>
 * Provides a method to retrieve endpoint URLs by their keys.
 */
public class Routes {
    private static final Map<String, String> apiRoutes = Map.of(
            "baseURI", "https://automationintesting.online/api",
            "postAuthEndpoint", "/auth/login",
            "postAuthEndpointInvalid", "/auto/login",
            "createBookingEndpoint", "/booking",
            "createBookingEndpointInvalid", "/book",
            "getUpdateDeleteBookingEndpoint", "/booking/{id}",
            "getUpdateDeleteBookingEndpointInvalid", "/book/{id}",
            "getHealthEndpoint", "/booking/actuator/health"
    );
    /**
     * Retrieves the API endpoint path based on the given key.
     *
     * @param endPointKey the key identifying the desired endpoint
     * @return the endpoint path if found, otherwise an empty string
     */
    public static String getEndpoint(String endPointKey) {
        return apiRoutes.getOrDefault(endPointKey, "");
    }
}
