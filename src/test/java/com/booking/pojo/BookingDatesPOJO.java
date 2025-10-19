package com.booking.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A POJO that represents booking date details.
 */
@Getter
@Setter
@AllArgsConstructor
public class BookingDatesPOJO {
    /** Check-in date in YYYY-MM-DD format. */
    private String checkin;
    /** Check-out date in YYYY-MM-DD format. */
    private String checkout;
}
