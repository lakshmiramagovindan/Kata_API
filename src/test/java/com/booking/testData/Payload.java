package com.booking.testData;

import com.booking.pojo.AuthPOJO;
import com.booking.pojo.BookingPOJO;

/**
 * Utility class for creating payload objects for API requests.
 */
public class Payload {
    /**
     * Creates an authentication payload with the given username and password.
     *
     * @param username the username for authentication
     * @param password the password for authentication
     * @return an AuthPOJO containing the provided credentials
     */
    public AuthPOJO authPayload(String username, String password) {
        return new AuthPOJO(username, password);
    }

    /**
     * Returns the given booking payload.
     *
     * @param bookingPOJO the booking details
     * @return the same BookingPOJO instance
     */
    public BookingPOJO bookingPayload(BookingPOJO bookingPOJO) {
        return bookingPOJO;
    }
}
