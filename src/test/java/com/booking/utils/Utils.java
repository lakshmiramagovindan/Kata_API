package com.booking.utils;

import com.booking.routes.Routes;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

public class Utils {
    public static RequestSpecification req;
    public static Response res;
    static Properties prop;
    private static final String configFilePath = "src/test/resources/config/config.properties";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static RequestSpecification baseRequestSpecification() {
        if (req == null) {
            req = new RequestSpecBuilder()
                    .setBaseUri(Routes.getEndpoint("baseURI"))
                    .addFilter(new RequestLoggingFilter(System.out))
                    .addFilter(new ResponseLoggingFilter(System.out))
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .build();
        }
        return req;
    }

    public static String validHttpMethodCheck(String httpMethod) {
        List<String> supportedMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH");
        String method = httpMethod.toUpperCase();
        if (!supportedMethods.contains(method)) {
            throw new IllegalArgumentException(String.format("Unsupported HTTP method %s", httpMethod));
        }
        return method;
    }

    public static String validEndPointCheck(String endPointKey) {
        String endPoint = Routes.getEndpoint(endPointKey);
        if (endPoint.isEmpty()) {
            throw new IllegalArgumentException(String.format("Unsupported endPointKey %s", endPointKey));
        }
        return endPoint;
    }

    public static String requestWithOrWithoutAuthentication(String configKey) throws IOException {
        String token = "";
        if (!configKey.isEmpty()) {
             token = readPropertyFile(configKey);
        }
        return token;
    }

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

    public static String readPropertyFile(String configKey) throws IOException {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
            return prop.getProperty(configKey);
        }
    }

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
