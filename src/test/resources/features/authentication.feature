Feature: To validate the authentication for the API endpoints

  Scenario: To verify the authentication end point with valid credentials
    Given User writes the authentication payload
    When User make a "POST" request on the "/auth/login" endpoint
    Then API call should be successful with status code 200
