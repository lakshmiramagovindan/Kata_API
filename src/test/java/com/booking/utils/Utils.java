package com.booking.utils;

import com.booking.routes.Routes;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Utility class providing helper methods for API testing,
 * including request setup, configuration management, and data processing.
 */
public class Utils {
    /** Properties object to read/write config properties. */
    static Properties prop;

    /** Path to the configuration properties file. */
    private static final String configFilePath = "src/test/resources/config/config.properties";

    /** Formatter for date strings in yyyy-MM-dd format. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Creates or returns a base RequestSpecification with common settings.
     * Includes base URI, JSON content type, and logging filters.
     *
     * @return the configured RequestSpecification
     */
    public static RequestSpecification baseRequestSpecification() {
            return new RequestSpecBuilder()
                    .setBaseUri(Routes.getEndpoint("baseURI"))
                    .addFilter(new RequestLoggingFilter(System.out))
                    .addFilter(new ResponseLoggingFilter(System.out))
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .build();
    }

    /**
     * Validates and returns the HTTP method if supported.
     *
     * @param httpMethod HTTP method string (e.g., GET, POST)
     * @return uppercase valid HTTP method
     * @throws IllegalArgumentException if method is unsupported
     */
    public static String validHttpMethodCheck(String httpMethod) {
        List<String> supportedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
        String method = httpMethod.toUpperCase();
        if (!supportedMethods.contains(method)) {
            throw new IllegalArgumentException(String.format("Unsupported HTTP method %s", httpMethod));
        }
        return method;
    }

    /**
     * Retrieves and validates an API endpoint by its key.
     *
     * @param endPointKey the key representing the endpoint
     * @return the endpoint string
     * @throws IllegalArgumentException if the key is invalid
     */
    public static String validEndPointCheck(String endPointKey) {
        String endPoint = Routes.getEndpoint(endPointKey);
        if (endPoint.isEmpty()) {
            throw new IllegalArgumentException(String.format("Unsupported endPointKey %s", endPointKey));
        }
        return endPoint;
    }

    /**
     * Reads a token or value from the config file if the key is not empty.
     *
     * @param configKey the key to look up
     * @return the corresponding token or empty string if key is empty
     * @throws IOException if reading config fails
     */
    public static String requestWithOrWithoutAuthentication(String configKey) throws IOException {
        String token = "";
        if (!configKey.isEmpty()) {
             token = readPropertyFile(configKey);
        }
        return token;
    }

    /**
     * Writes a key-value pair to the config properties file.
     *
     * @param configKey   the key to write
     * @param configValue the value to associate with the key
     * @throws IOException if writing fails
     */
    public static void writePropertyFile(String configKey, String configValue) throws IOException {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
        }
        prop.setProperty(configKey, configValue);
        try(FileOutputStream fos = new FileOutputStream(configFilePath)) {
            prop.store(fos, null);
        }
    }

    /**
     * Reads the value associated with the given key from the config properties file.
     *
     * @param configKey the key to read
     * @return the value as a string
     * @throws IOException if reading fails
     */
    public static String readPropertyFile(String configKey) throws IOException {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
            return prop.getProperty(configKey);
        }
    }

    /**
     * Processes a date string. Supports special keywords:
     * - "TODAY" returns current date
     * - "+N" returns current date plus N days
     * Otherwise returns the original value.
     *
     * @param value the input date string or keyword
     * @return the processed date string in yyyy-MM-dd format
     */
    public static String processDate(String value) {
        String result;
        if ("TODAY".equalsIgnoreCase(value)) {
            result = LocalDate.now().format(FORMATTER);
        } else if (value.startsWith("+")) {
            int days = Integer.parseInt(value.substring(1));
            result = LocalDate.now().plusDays(days).format(FORMATTER);
        } else {
            result = value;
        }
        return result;
    }

    /**
     * Converts an actual value from the response into a string.
     * Joins lists into comma-separated strings, otherwise calls toString.
     *
     * @param actual the value to convert
     * @return string representation of the value
     */
    public static String convertActualValue(Object actual) {
        return (actual instanceof List<?>)
                ? ((List<?>) actual).stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                : String.valueOf(actual);
    }

    public static String sanitizeDateValue(String value) {
        return (value == null) ? "" : value;
    }

    public static int parseRoomId(String value) {
        int roomId;

        if ("RANDOM_1_10".equalsIgnoreCase(value)) {
            roomId = new Random().nextInt(10) + 1;
        } else {
            roomId = 0;
        }
        return roomId;
    }
}
