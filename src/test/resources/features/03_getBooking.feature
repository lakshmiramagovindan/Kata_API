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
      | checkin     | +3                   |
      | checkout    | +4                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User makes a "POST" action to the "createBookingEndpoint" with ""
    Then The system should respond with status "201"
    And The system should respond with description "Created"
    And Store "bookingid" from response as "bookingID" in config file

  @getBooking @positive
  Scenario: To verify the get booking endpoint with valid parameters.
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "GET" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "200"
    Then The system should respond with description "OK"
    Then the response fields should match:
      | field                 | condition | expected value       |
      | bookingid             | not empty |                      |
      | roomid                | not empty |                      |
      | firstname             | equals    | John                 |
      | lastname              | equals    | Doe                  |
      | depositpaid           | equals    | true                 |
      | bookingdates.checkin  | not empty |                      |
      | bookingdates.checkout | not empty |                      |
      #Note: Validations from lines 20-21 will fail since API is not designed to have email, phone as part of response.Scenario:
    #Booking.yaml mentions email and phone also as part of our requests
      | email                 | equals    | john.doe@example.com |
      | phone                 | equals    | 12345678901          |
    And Response should match the "createBookingResponseSchema" schema

  @getBooking @negative
  Scenario Outline: To verify the get booking endpoint with invalid request specifications.
    Given User sends basic information "<parameter>" and the login token "<cookieToken>"
    When User makes a "GET" action to the "<endPoint>" with "<authKey>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"

    Examples:
      | parameter      | cookieToken | endPoint                              | authKey   | statusCode | description |
      | bookingIDDummy | authToken   | getUpdateDeleteBookingEndpoint        | authToken | 404        | Not Found   |
      | bookingID      |             | getUpdateDeleteBookingEndpoint        | authToken | 403        | Forbidden   |
      | bookingID      | authToken   | getUpdateDeleteBookingEndpointInvalid | authToken | 404        | Not Found   |
      #Note: Test cases (lines 39) may fail due to API not configured to return proper status code when token is "".
      # Expected: 403 with "Forbidden"
      # Actual: Random Status Codes
      | bookingID      | authToken   | getUpdateDeleteBookingEndpoint        |           | 403        | Forbidden   |

  @deleteBooking
  Scenario: To delete the created booking
    Comments: This scenario is added to ensure there are no conflicts to roomid in the next creation
    Given User sends basic information "bookingID" and the login token "authToken"
    When User makes a "DELETE" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "200"
    And The system should respond with description "OK"
    And "success" in response body should be "true"