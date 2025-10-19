package com.booking.stepDefinitions;

import com.booking.pojo.BookingDatesPOJO;
import com.booking.pojo.BookingPOJO;
import com.booking.testData.Payload;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import static com.booking.utils.Utils.*;
import java.util.Map;

public class BookingStepDefinition {
    private final Payload payload = new Payload();

    @Given("User creates or modifies booking with:")
    public void userCreatesBookingWith(DataTable dataTable) {
        Map<String, String> input = dataTable.asMap(String.class, String.class);

        BookingDatesPOJO bookingDates = new BookingDatesPOJO(
                processDate(sanitizeDateValue(input.get("checkin"))),
                processDate(sanitizeDateValue(input.get("checkout")))
        );
        BookingPOJO booking = new BookingPOJO();
        booking.setRoomid(parseRoomId(input.get("roomid")));
        booking.setFirstname(input.get("firstname"));
        booking.setLastname(input.get("lastname"));
        booking.setDepositpaid(Boolean.parseBoolean(input.get("depositpaid")));
        booking.setBookingdates(bookingDates);
        booking.setEmail(input.get("email"));
        booking.setPhone(input.get("phone"));
        req = req
                .body(payload.bookingPayload(booking));
    }
}
