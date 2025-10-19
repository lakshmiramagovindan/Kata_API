Feature: To validate the create booking API endpoint

  @createBooking @positive
  Scenario: To verify the create booking endpoint with valid payload.
    Given User creates booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Doe                  |
      | depositpaid | true                 |
      | checkin     | TODAY                |
      | checkout    | +2                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User makes a "POST" action to the "createBookingEndpoint"
    Then The system should respond with status "201"
    Then The system should respond with description "Created"
    And "bookingid" in response should not be empty
    And Store "bookingid" from response as "bookingID" in config file
    And Response should match the "createBookingResponseSchema" schema
    And "roomid" in response should not be empty
    And "firstname" in response body should be "John"
    And "lastname" in response body should be "Doe"
    And "depositpaid" in response body should be "true"
    And "bookingdates.checkin" in response should not be empty
    And "bookingdates.checkout" in response should not be empty
    #Note: Validations from lines 29–30 will fail since API is not designed to have email, phone as part of response.Scenario:
    #Booking.yaml mentions email and phone also as part of our requests
    And "email" in response body should be "john.doe@example.com"
    And "phone" in response body should be "12345678901"

  @createBooking @negative
  Scenario Outline: To verify the negative scenarios of create booking end point by passing incorrect payload
    Given User creates booking with:
      | key         | value         |
      | roomid      | <roomid>      |
      | firstname   | <firstname>   |
      | lastname    | <lastname>    |
      | depositpaid | <depositpaid> |
      | checkin     | <checkin>     |
      | checkout    | <checkout>    |
      | email       | <email>       |
      | phone       | <phone>       |
    When User makes a "POST" action to the "createBookingEndpoint"
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
      # Note: Test cases (lines 64–65 ) may fail due different Lastname upper limit.
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
      # Note: Test cases (lines 72) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@example.com |                        | Phone should not be blank           |
      #Email Validation
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doe@            | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | @example.com         | 12345678901            | must be a well-formed email address |
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       | john.doeexample.com  | 12345678901            | must be a well-formed email address |
      # Note: Test cases (lines 79) may fail due to API not configured to return
            # standard error message for missing mandatory fields.
      | RANDOM_1_10 | John                | Doe                 | true        | +1      | +2       |                      | 12345678901            | Email should not be blank           |
      #Dates Validation
      # Note: Test cases (lines 84–89) may fail due to checkout and checkin date not properly handled.
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
  Scenario Outline: To verify the create booking endpoint with valid payload and invalid request specifications.
    Given User creates booking with:
      | key         | value                |
      | roomid      | RANDOM_1_10          |
      | firstname   | John                 |
      | lastname    | Doe                  |
      | depositpaid | true                 |
      | checkin     | TODAY                |
      | checkout    | +2                   |
      | email       | john.doe@example.com |
      | phone       | 12345678901          |
    When User makes a "<httpMethod>" action to the "<endPoint>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"

    Examples:
      | httpMethod | endPoint                     | statusCode | description        |
      | POST       | createBookingEndpointInvalid | 404        | Not Found          |
      | GET        | createBookingEndpoint        | 401        | Unauthorized       |
      | PUT        | createBookingEndpoint        | 405        | Method Not Allowed |
      | DELETE     | createBookingEndpoint        | 405        | Method Not Allowed |
      | PATCH      | createBookingEndpoint        | 405        | Method Not Allowed |