package org.appointmentschedulingsystem.dtos;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.util.enums.Role;
import org.springframework.data.annotation.Id;
import java.util.List;

@Getter
@Setter
public class UserDto {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Email(message = "Please provide a valid email address")
    private String email;
    private String password;
    private String address;
    private byte[] image;
    private Role role;
    private LocationDto location;
    private List<AppointmentDto> bookAppointments;

}
