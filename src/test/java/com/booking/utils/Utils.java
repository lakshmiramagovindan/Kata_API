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
import java.util.List;
import java.util.Properties;

public class Utils {
    public static RequestSpecification req;
    public static Response res;
    static Properties prop;
    private static final String configFilePath = "C:\\Users\\lsree\\API_Testing_kata\\src\\test\\resources\\config\\config.properties";

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
             token = readWritePropertyFile(configKey);
        }
        return token;
    }

    public static void readWritePropertyFile(String configKey, String configValue) throws IOException {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
        }
        prop.setProperty(configKey, configValue);
        try(FileOutputStream fos = new FileOutputStream(configFilePath)) {
            prop.store(fos, null);
        }
    }

    public static String readWritePropertyFile(String configKey) throws IOException {
        prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            prop.load(fis);
            return prop.getProperty(configKey);
        }
    }
}
