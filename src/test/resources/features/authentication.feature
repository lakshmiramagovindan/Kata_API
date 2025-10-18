Feature: To validate the authentication for the API endpoints

  @authentication @positive @negative
  Scenario Outline: To verify the authentication end point with valid and invalid credentials
    Given User writes the authentication payload with "<username>" and "<password>"
    When User makes a "<httpMethod>" request on the "<endPoint>" endpoint
    Then The API status code should be "<statusCode>"

    Examples:
    | username | password | httpMethod | endPoint           | statusCode |
    | admin    | password | POST       | /auth/login        | 200        |
    | user1    | password | POST       | /auth/login        | 401        |
    | admin    | p@sswOrd | POST       | /auth/login        | 401        |
    | admin    | password | GET        | /auth/login        | 405        |
    | admin    | password | POST       | /auto/login        | 404        |
    # Note: The following test cases (line no 17-19) may fail as the API is not yet configured to return
    # 400 Bad Request for missing mandatory input fields.
    |          | password | POST       | /auth/login        | 400        |
    | admin    |          | POST       | /auth/login        | 400        |
    |          |          | POST       | /auth/login        | 400        |

  @authentication @positive
  Scenario: To verify the authentication end point with valid credentials
    Given User writes the authentication payload with "admin" and "password"
    When User makes a "POST" request on the "/auth/login" endpoint
    Then The API status code should be "200"