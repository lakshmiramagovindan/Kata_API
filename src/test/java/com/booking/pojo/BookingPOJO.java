package com.booking.pojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A POJO that represents a booking record.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPOJO {
    /** ID of the booked room */
    private int roomid;
    /** Guest's first name. */
    private String firstname;
    /** Guest's last name. */
    private String lastname;
    /** Indicates if a deposit has been paid. */
    private Boolean depositpaid;
    /** Object containing check-in and check-out dates. */
    private BookingDatesPOJO bookingdates;
    /** Guest's email address. */
    private String email;
    /** Guest's phone number. */
    private String phone;
}
