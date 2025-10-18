package com.booking.testData;

import com.booking.pojo.AuthPOJO;

public class Payload {

    public AuthPOJO authPayload(String username, String password) {
        return new AuthPOJO(username, password);
    }
}
