package com.booking.pojo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple POJO class that holds authentication details.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthPOJO {
    /** Username for authentication. */
    private String username;
    /** Password for authentication. */
    private String password;
}
