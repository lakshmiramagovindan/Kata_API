Feature: To validate the get booking API endpoint

  @getBooking @positive
  Scenario: To verify the get booking endpoint with valid parameters.
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "GET" action to the "getUpdateDeleteBookingEndpoint" via "authToken"
    Then The system should respond with status "200"
    Then The system should respond with description "OK"
    And "bookingid" in response should not be empty
    And "roomid" in response should not be empty
    And "firstname" in response body should be "John"
    And "lastname" in response body should be "Doe"
    And "depositpaid" in response body should be "true"
    And "bookingdates.checkin" in response should not be empty
    And "bookingdates.checkout" in response should not be empty
    And Response should match the "createBookingResponseSchema" schema
    #Note: Validations from lines 19-20 will fail since API is not designed to have email, phone as part of response.Scenario:
    #Booking.yaml mentions email and phone also as part of our requests
    And "email" in response body should be "john.doe@example.com"
    And "phone" in response body should be "12345678901"

  @getBooking @negative
  Scenario Outline: To verify the get booking endpoint with invalid request specifications.
    Given User sends basic information "<parameter>" and the login token "<cookieToken>"
    When User makes a "<httpMethod>" action to the "<endPoint>" via "<authKey>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"

    Examples:
      | parameter      | cookieToken    | httpMethod | endPoint                              | authKey        | statusCode | description        |
      | bookingIDDummy | authToken      | GET        | getUpdateDeleteBookingEndpoint        | authToken      | 404        | Not Found          |
      | bookingID      | authTokenDummy | GET        | getUpdateDeleteBookingEndpoint        | authToken      | 403        | Forbidden          |
      | bookingID      | authToken      | GET        | getUpdateDeleteBookingEndpointInvalid | authToken      | 404        | Not Found          |
      #Note: Test cases (lines 37) may fail due to API not configured to return proper status code when token is incorrect.
      # Expected: 403 with "Forbidden"
      # Actual: Random Status Codes
      | bookingID      | authToken      | GET        | getUpdateDeleteBookingEndpoint        | authTokenDummy | 403        | Forbidden          |
      | bookingID      | authToken      | POST       | getUpdateDeleteBookingEndpoint        | authToken      | 405        | Method Not Allowed |
      #Note: Test cases (lines 42) may fail due to API not configured to return proper status code when payload is not passed.
      # Expected: 400 with "Bad Request"
      # Actual: 500 with "Internal Server Error"
      | bookingID      | authToken      | PUT        | getUpdateDeleteBookingEndpoint        | authToken      | 400        | Bad Request        |
      | bookingID      | authToken      | PATCH      | getUpdateDeleteBookingEndpoint        | authToken      | 405        | Method Not Allowed |