Feature: To validate the authentication for the API endpoints

  @authentication @positive
  Scenario: To verify the authentication end point with valid credentials and perform schema validation
    Given User writes the authentication payload with "admin" and "password"
    When User makes a "POST" request on the "postAuthEndpoint" endpoint
    Then The API status code should be "200"
    And "token" in response body should not be empty
    Then Response should match the "authenticationResponseSchema" schema

  @authentication @negative
  Scenario Outline: To verify the authentication end point with negative status codes
    Given User writes the authentication payload with "<username>" and "<password>"
    When User makes a "<httpMethod>" request on the "<endPoint>" endpoint
    Then The API status code should be "<statusCode>"

    Examples:
    | username | password | httpMethod | endPoint                | statusCode |
    | user1    | password | POST       | postAuthEndpoint        | 401        |
    | admin    | p@sswOrd | POST       | postAuthEndpoint        | 401        |
    | admin    | password | GET        | postAuthEndpoint        | 405        |
    | admin    | password | POST       | postAuthEndpointInvalid | 404        |
    # Note: The following test cases (line no 17-19) may fail as the API is not yet configured to return
    # 400 Bad Request for missing mandatory input fields.
    |          | password | POST       | postAuthEndpoint        | 400        |
    | admin    |          | POST       | postAuthEndpoint        | 400        |
    |          |          | POST       | postAuthEndpoint        | 400        |