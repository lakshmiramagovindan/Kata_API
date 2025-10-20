package com.booking.stepDefinitions;

import com.booking.context.ScenarioContextHolder;
import com.booking.pojo.BookingDatesPOJO;
import com.booking.pojo.BookingPOJO;
import com.booking.testData.PayloadFactory;
import com.booking.utils.Utils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.Map;

import static com.booking.utils.Utils.*;

public class RequestStepDefinitions {


    /** Payload utility for building request bodies. */
    private final PayloadFactory payload = new PayloadFactory();
    /**
     * Cucumber step: Logs in the user with the given username and password.
     * <p>
     * Sets the request body using the authentication payload.
     *
     * @param username the username for login
     * @param password the password for login
     */
    @Given("User logs in with {string} and {string}")
    public void userLogsInWith(String username, String password) {
        ScenarioContextHolder.getContext()
                .setReq(
                        ScenarioContextHolder.getContext()
                                .getReq()
                                .body(payload.createAuthPayload(username, password)));
    }
    @Given("User creates or modifies booking with:")
    public void userCreatesBookingWith(DataTable dataTable) {
        Map<String, String> input = dataTable.asMap(String.class, String.class);

        BookingDatesPOJO bookingDates = new BookingDatesPOJO(
                Utils.processDate(Utils.sanitizeDateValue(input.get("checkin"))),
                Utils.processDate(Utils.sanitizeDateValue(input.get("checkout")))
        );
        BookingPOJO booking = new BookingPOJO();
        booking.setRoomid(Utils.parseRoomId(input.get("roomid")));
        booking.setFirstname(input.get("firstname"));
        booking.setLastname(input.get("lastname"));
        booking.setDepositpaid(Boolean.parseBoolean(input.get("depositpaid")));
        booking.setBookingdates(bookingDates);
        booking.setEmail(input.get("email"));
        booking.setPhone(input.get("phone"));
        ScenarioContextHolder.getContext()
                .setReq(
                        ScenarioContextHolder.getContext()
                                .getReq()
                                .body(payload.createBookingPayload(booking)));
    }
    @When("User makes a {string} action to the {string} with {string}")
    public void userMakesAActionToTheEndpoint(String httpMethod, String endPointKey, String authPart) throws IOException {
        String method = validHttpMethodCheck(httpMethod);
        String endPoint = validEndPointCheck(endPointKey);

        RequestSpecification req = ScenarioContextHolder.getContext().getReq();
        Response res;

        // If authPart is like "authToken", extract token key; else no auth
        if (authPart != null && !authPart.trim().isEmpty()) {
            String token = requestWithOrWithoutAuthentication(authPart);

            res = req
                    .auth().oauth2(token)
                    .when()
                    .request(method, endPoint);
        } else {
            res = req
                    .when()
                    .request(method, endPoint);
        }

        ScenarioContextHolder.getContext().setRes(res);
    }

    /**
     * Prepares the request with a path parameter and login token as a cookie.
     *
     * @param pathParam   the key for the path parameter value from config
     * @param cookieToken the key for the token to be added in the Cookie header
     */
    @Given("User sends basic information {string} and the login token {string}")
    public void userSendsBasicInformationAndTheLoginToken(String pathParam, String cookieToken) throws IOException {
        String token = requestWithOrWithoutAuthentication(cookieToken);
        RequestSpecification req = ScenarioContextHolder.getContext().getReq();
        if (!pathParam.isBlank()) {
            String pathParamValue = readPropertyFile(pathParam);
            req = req.pathParam("id", pathParamValue);
        }

        // Always apply cookie token
        req = req.header("Cookie", "token=" + token);

        ScenarioContextHolder.getContext().setReq(req);
    }
}
