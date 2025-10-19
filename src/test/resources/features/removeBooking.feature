Feature: To validate the get booking API endpoint

  @deleteBooking @positive
  Scenario: To verify the delete booking endpoint with valid parameters.
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "DELETE" action to the "getUpdateDeleteBookingEndpoint" via "authToken"
    Then The system should respond with status "200"
    Then The system should respond with description "OK"
    And "success" in response body should be "true"
    And Response should match the "deleteResponseSchema" schema

  @deleteBooking @negative
  Scenario Outline: To verify the delete booking endpoint with invalid request specifications.
    Given User sends basic information "<parameter>" and the login token "<cookieToken>"
    When User makes a "<httpMethod>" action to the "<endPoint>" via "<authKey>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"

    Examples:
      | parameter      | cookieToken    | httpMethod | endPoint                              | authKey        | statusCode | description        |
      #Note: Test cases (lines 24-25) may fail due to API not configured to return proper status code when request parameters are incorrect.
      # Expected: 404 when query param is wrong. 403 when cookie is wrong
      # Actual: 500 with "Internal Server Error"
      | bookingIDDummy | authToken      | DELETE     | getUpdateDeleteBookingEndpoint        | authToken      | 404        | Not Found          |
      | bookingID      | authTokenDummy | DELETE     | getUpdateDeleteBookingEndpoint        | authToken      | 403        | Forbidden          |
      | bookingID      | authToken      | DELETE     | getUpdateDeleteBookingEndpointInvalid | authToken      | 404        | Not Found          |
      #Note: Test cases (lines 30) may fail due to API not configured to return proper status code when token is wrong
      # Expected: 403 with "Forbidden"
      # Actual: Random Status Codes
      | bookingID      | authToken      | DELETE     | getUpdateDeleteBookingEndpoint        | authTokenDummy | 403        | Forbidden          |
      | bookingID      | authToken      | POST       | getUpdateDeleteBookingEndpoint        | authToken      | 405        | Method Not Allowed |
      #Note: Test cases (lines 35) may fail due to API not configured to return proper status code when payload is not passed.
      # Expected: 400 with "Bad Request"
      # Actual: 500 with "Internal Server Error"
      | bookingID      | authToken      | PUT        | getUpdateDeleteBookingEndpoint        | authToken      | 400        | Bad Request        |
      | bookingID      | authToken      | PATCH      | getUpdateDeleteBookingEndpoint        | authToken      | 405        | Method Not Allowed |

