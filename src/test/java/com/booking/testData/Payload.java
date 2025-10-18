package com.booking.testData;

import com.booking.pojo.AuthPOJO;

public class Payload {

    public AuthPOJO authPayload(String username, String password) {
        AuthPOJO authPOJO = new AuthPOJO();
        authPOJO.setUsername(username);
        authPOJO.setPassword(password);

        return authPOJO;
    }
}
