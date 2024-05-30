package org.appointmentschedulingsystem.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class JwtResponse {

    private String JwtToken;
    private String userName;

}

