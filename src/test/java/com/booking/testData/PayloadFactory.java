package com.booking.testData;

import com.booking.pojo.AuthPOJO;
import com.booking.pojo.BookingPOJO;

/**
 * Factory class for creating payload objects for API requests.
 */
public class PayloadFactory {

    /**
     * Creates an authentication payload with the given username and password.
     *
     * @param username the username for authentication
     * @param password the password for authentication
     * @return an AuthPOJO containing the provided credentials
     */
    public AuthPOJO createAuthPayload(String username, String password) {
        return new AuthPOJO(username, password);
    }

    /**
     * Returns the given booking payload.
     *
     * @param bookingPOJO the booking details
     * @return the same BookingPOJO instance
     */
    public BookingPOJO createBookingPayload(BookingPOJO bookingPOJO) {
        return bookingPOJO;
    }
}
