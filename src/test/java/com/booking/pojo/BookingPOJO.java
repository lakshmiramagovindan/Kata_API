package com.booking.pojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingPOJO {
    private int roomid;
    private String firstname;
    private String lastname;
    private Boolean depositpaid;
    private BookingDatesPOJO bookingdates;
    private String email;
    private String phone;
}
