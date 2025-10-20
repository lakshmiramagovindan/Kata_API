Feature: To validate the create booking API endpoint

  @createBooking @positive
  Scenario: To verify the create booking endpoint with valid payload.
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
    When User makes a "POST" action to the "createBookingEndpoint" with ""
    Then The system should respond with status "201"
    Then the response fields should match:
      | field                 | condition | expected value       |
      | description           | equals    | Created              |
      | bookingid             | not empty |                      |
      | roomid                | not empty |                      |
      | firstname             | equals    | John                 |
      | lastname              | equals    | Doe                  |
      | depositpaid           | equals    | true                 |
      | bookingdates.checkin  | not empty |                      |
      | bookingdates.checkout | not empty |                      |
      #Note: Validations from lines 29-30 will fail since API is not designed to have email, phone as part of response.Scenario:
    #Booking.yaml mentions email and phone also as part of our requests
      | email                 | equals    | john.doe@example.com |
      | phone                 | equals    | 12345678901          |
    And Response should match the "createBookingResponseSchema" schema

  @createBooking @negative
  Scenario Outline: To verify the negative scenarios of create booking end point by passing incorrect payload
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
    When User makes a "POST" action to the "createBookingEndpoint" with ""
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
      # Note: Test cases (lines 65–66 ) may fail due different Lastname upper limit.
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
      # Note: Test cases (lines 73) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@example.com |                        | Phone should not be blank           |
      #Email Validation
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@            | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | @example.com         | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doeexample.com  | 12345678901            | must be a well-formed email address |
      # Note: Test cases (lines 80) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       |                      | 12345678901            | Email should not be blank           |
      #Dates Validation
      # Note: Test cases (lines 85–90) may fail due to checkout and checkin date not properly handled.
      # - Expected: 400 with "Failed to create booking"
      # - Actual: 409 with "Failed to create booking"
      | RANDOM_1_10 | John                | Doe                 | true        | +2      | +1       | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | TODAY    | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        | TODAY   | TODAY    | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        |         | +1       | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        | +2      |          | john.doe@example.com | 12345678901            | Failed to create booking            |
      | RANDOM_1_10 | John                | Doe                 | true        |         |          | john.doe@example.com | 12345678901            | Failed to create booking            |
      #All blank fields
      |             |                     |                     |             |         |          |                      |                        | Failed to create booking            |

  @createBooking @negative
  Scenario: To verify the create booking endpoint with valid payload and invalid request specifications.
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
    When User makes a "POST" action to the "createBookingEndpointInvalid" with ""
    Then The system should respond with status "404"
    Then The system should respond with description "Not Found"