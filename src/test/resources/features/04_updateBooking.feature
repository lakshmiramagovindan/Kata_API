Feature: To validate the update booking API endpoint

  @createBooking
  Scenario: To create booking with valid payload.
  Comments: This scenario is added to have a separate booking id to avoid interdependency between feature files
    Given User creates or modifies booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Doe                  |
      | depositpaid | true                 |
      | checkin     | +6                   |
      | checkout    | +7                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User makes a "POST" action to the "createBookingEndpoint" with ""
    Then The system should respond with status "201"
    And Store "bookingid" from response as "bookingID" in config file

  @updateBooking @positive @e2e
  Scenario: To verify the update booking endpoint with valid payload.
    Given User creates or modifies booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Change               |
      | depositpaid | false                |
      | checkin     | +5                   |
      | checkout    | +6                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User sends basic information "bookingID" and the login token "authToken"
    When User makes a "PUT" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "200"
    Then The system should respond with description "OK"
    And "success" in response body should be "true"
    And Response should match the "deleteResponseSchema" schema

  @updateBooking @negative
  Scenario Outline: To verify the negative scenarios of update booking end point by passing incorrect payload
    Given User creates or modifies booking with:
      | key         | value         |
      | roomid      | <roomid>      |
      | firstname   | <firstname>   |
      | lastname    | <lastname>    |
      | depositpaid | <depositpaid> |
      | checkin     | <checkin>     |
      | checkout    | <checkout>    |
      | email       | <email>       |
      | phone       | <phone>       |
    When User sends basic information "bookingID" and the login token "authToken"
    When User makes a "PUT" action to the "getUpdateDeleteBookingEndpoint" with "authToken"
    Then The system should respond with status "400"
    Then The system should respond with description "Bad Request"
    Then "errors" in response body should be "<message>"
    And Response should match the "createBookingErrorSchema" schema

    Examples:
      | roomid      | firstname           | lastname            | depositpaid | checkin | checkout | email                | phone                  | message                             |
      #Roomid Validation
      |             | John                | Doe                 | true        | +1      | +2       | john.doe@example.com | 12345678901            | must be greater than or equal to 1  |
      #FirstName Validation
      | RANDOM_1_10 | Jo                  | Doe                 | true        | +1      | +2       | john.doe@example.com | 12345678901            | size must be between 3 and 18       |
      | RANDOM_1_10 | Johnjikmuhtfyhtjgcm | Doe                 | true        | +1      | +2       | john.doe@example.com | 12345678901            | size must be between 3 and 18       |
      | RANDOM_1_10 |                     | Doe                 | true        | +1      | +2       | john.doe@example.com | 12345678901            | Firstname should not be blank       |
      #LastName Validation
      # Note: Test cases (lines 55–56 ) may fail due different Lastname upper limit.
      # - Expected: 400 with "size must be between 3 and 18"
      # - Actual:
      #   For length < 3 → 400 with "size must be between 3 and 30"
      #   For length > 18 → 201 Created (incorrect validation using length > 30)
      | RANDOM_1_10 | John                | Do                  | true        | +1      | +2       | john.doe@example.com | 12345678901            | size must be between 3 and 18       |
      | RANDOM_1_10 | John                | Dohnjikmuhtfyhtjgcm | true        | +1      | +2       | john.doe@example.com | 12345678901            | size must be between 3 and 18       |
      | RANDOM_1_10 | John                |                     | true        | +1      | +2       | john.doe@example.com | 12345678901            | Lastname should not be blank        |
      #PhoneNumber Validation
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@example.com | 1234567890             | size must be between 11 and 21      |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@example.com | 1234567890112345678901 | size must be between 11 and 21      |
      # Note: Test cases (lines 63) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@example.com |                        | Phone should not be blank           |
      #Email Validation
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@            | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | @example.com         | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doeexample.com  | 12345678901            | must be a well-formed email address |
      # Note: Test cases (lines 70) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       |                      | 12345678901            | Email should not be blank           |
      #Dates Validation
      # Note: Test cases (lines 75–77) may fail due to checkout and checkin date not properly handled.
      # - Expected: 400 with "Failed to create booking"
      # - Actual: 409 with "Failed to create booking"
      | RANDOM_1_10 | John                | Doe                 | true        | +2      | +1       | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | TODAY    | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        | TODAY   | TODAY    | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        |         | +1       | john.doe@example.com | 12345678901            | must not be null                    |
      | RANDOM_1_10 | John                | Doe                 | true        | +2      |          | john.doe@example.com | 12345678901            | must not be null                    |
      | RANDOM_1_10 | John                | Doe                 | true        |         |          | john.doe@example.com | 12345678901            | must not be null, must not be null  |
      #All blank fields
      |             |                     |                     |             |         |          |                      |                        | Failed to update booking            |

  @updateBooking @negative
  Scenario Outline: To verify the update booking endpoint with valid payload and invalid request specifications.
    Given User creates or modifies booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Doe                  |
      | depositpaid | true                 |
      | checkin     | TODAY                |
      | checkout    | +2                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User sends basic information "<parameter>" and the login token "<cookieToken>"
    When User makes a "PUT" action to the "<endPoint>" with "<authKey>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"

    Examples:
      | parameter      | cookieToken | endPoint                              | authKey   | statusCode | description |
      | bookingIDDummy | authToken   | getUpdateDeleteBookingEndpoint        | authToken | 404        | Not Found   |
      | bookingID      |             | getUpdateDeleteBookingEndpoint        | authToken | 403        | Forbidden   |
      | bookingID      | authToken   | getUpdateDeleteBookingEndpointInvalid | authToken | 404        | Not Found   |
      #Note: Test cases (lines 109) may fail due to API not configured to return proper status code when token is incorrect
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