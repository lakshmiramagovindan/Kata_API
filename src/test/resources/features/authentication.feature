Feature: To validate the authentication for the API endpoints

  @authentication @positive
  Scenario: To verify the authentication end point with valid credentials and perform schema validation
    Given User logs in with "admin" and "password"
    When User makes a "POST" action to the "postAuthEndpoint"
    Then The system should respond with status "200"
    Then The system should respond with description "OK"
    And "token" in response should not be empty
    And Response should match the "authenticationResponseSchema" schema
    And Store "token" from response as "authToken" in config file

  @authentication @negative
  Scenario Outline: To verify the authentication end point with negative status codes
    Given User logs in with "<username>" and "<password>"
    When User makes a "<httpMethod>" action to the "<endPoint>"
    Then The system should respond with status "<statusCode>"
    Then The system should respond with description "<description>"
    And Response should match the "<schemaFileName>" schema

    Examples:
    | username | password | httpMethod | endPoint                | statusCode | description        | schemaFileName            |
    | user1    | password | POST       | postAuthEndpoint        | 401        | Unauthorized       | authenticationErrorSchema |
    | admin    | p@sswOrd | POST       | postAuthEndpoint        | 401        | Unauthorized       | authenticationErrorSchema |
    | admin    | password | GET        | postAuthEndpoint        | 405        | Method Not Allowed |                           |
    | admin    | password | PUT        | postAuthEndpoint        | 405        | Method Not Allowed |                           |
    | admin    | password | DELETE     | postAuthEndpoint        | 405        | Method Not Allowed |                           |
    | admin    | password | PATCH      | postAuthEndpoint        | 405        | Method Not Allowed |                           |
    | admin    | password | POST       | postAuthEndpointInvalid | 404        | Not Found          |                           |
    # Note: The following test cases (line no 32-34) may fail as the API is not yet configured to return
    # 400 Bad Request for missing mandatory input fields.
    |          | password | POST       | postAuthEndpoint        | 400        | Bad Request        |                           |
    | admin    |          | POST       | postAuthEndpoint        | 400        | Bad Request        |                           |
    |          |          | POST       | postAuthEndpoint        | 400        | Bad Request        |                           |