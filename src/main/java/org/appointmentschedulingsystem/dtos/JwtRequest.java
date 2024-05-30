package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtRequest {

    private String email;
    private String password;

}
