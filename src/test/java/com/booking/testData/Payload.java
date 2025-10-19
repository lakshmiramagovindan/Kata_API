package com.booking.testData;

import com.booking.pojo.AuthPOJO;
import com.booking.pojo.BookingPOJO;

public class Payload {

    public AuthPOJO authPayload(String username, String password) {
        return new AuthPOJO(username, password);
    }

    public BookingPOJO bookingPayload(BookingPOJO bookingPOJO) {
        return bookingPOJO;
    }
}
