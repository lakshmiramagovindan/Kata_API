package com.booking.utils;

import com.booking.routes.Routes;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {
    public static RequestSpecification req;
    public static Response res;
    public RequestSpecification requestSpecification() {
        req = new RequestSpecBuilder()
                .setBaseUri(Routes.getEndpoint("baseURI"))
                .addFilter(new RequestLoggingFilter(System.out))
                .addFilter(new ResponseLoggingFilter(System.out))
                .setContentType(ContentType.JSON)
                .build();
        return req;
    }
}
