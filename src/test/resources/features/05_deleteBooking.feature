Feature: To validate the get booking API endpoint

  @createBooking
  Scenario: To create booking with valid payload.
  Comments: This scenario is added to have a separate booking id to avoid interdependency between feature files
    Given User creates or modifies booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Doe                  |
      | depositpaid | true                 |
      | checkin     | +10                  |
      | checkout    | +11                  |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User makes a "POST" action to the "createBookingEndpoint" with ""
    Then The system should respond with status "201"
    And Store "bookingid" from response as "bookingID" in config file

  @deleteBooking @positive @e2e
  Scenario: To verify the delete booking endpoint with valid parameters.
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "DELETE" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "200"
    And The system should respond with description "OK"
    And "success" in response body should be "true"
    And Response should match the "deleteResponseSchema" schema

  @deleteBooking @negative
  Scenario Outline: To verify the delete booking endpoint with invalid request specifications.
    Given User sends basic information "<parameter>" and the login token "<cookieToken>"
    When User makes a "DELETE" action to the "<endPoint>" with "<authKey>"
    Then The system should respond with status "<statusCode>"
    And The system should respond with description "<description>"

    Examples:
      | parameter      | cookieToken | endPoint                              | authKey   | statusCode | description |
      #Note: Test cases (lines 24-25) may fail due to API not configured to return proper status code when request parameters are incorrect.
      # Expected: 404 when query param is wrong. 403 when cookie is wrong
      # Actual: 500 with "Internal Server Error"
      | bookingIDDummy | authToken   | getUpdateDeleteBookingEndpoint        | authToken | 404        | Not Found   |
      | bookingID      |             | getUpdateDeleteBookingEndpoint        | authToken | 403        | Forbidden   |
      | bookingID      | authToken   | getUpdateDeleteBookingEndpointInvalid | authToken | 404        | Not Found   |
      #Note: Test cases (lines 30) may fail due to API not configured to return proper status code when token is wrong
      # Expected: 403 with "Forbidden"
      # Actual: Random Status Codes
      | bookingID      | authToken   | getUpdateDeleteBookingEndpoint        |           | 403        | Forbidden   |

  @deleteBooking @negative
  Scenario: Verify behavior when retrieving a deleted booking record
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "GET" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "404"
    And The system should respond with description "Not Found"