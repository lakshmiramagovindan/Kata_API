Feature: To get a deleted record in API

@getBooking @positive
Scenario: To verify the behaviour of get booking endpoint when trying to retrieve deleted record.
Given User sends basic information "bookingID" and the login token "authToken"
When User makes a "GET" action to the "getUpdateDeleteBookingEndpoint" via "authToken"
Then The system should respond with status "404"
Then The system should respond with description "Not Found"