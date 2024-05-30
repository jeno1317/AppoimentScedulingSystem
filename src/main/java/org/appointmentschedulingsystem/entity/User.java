package org.appointmentschedulingsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appointmentschedulingsystem.util.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "AppointmentSchedulingSystem")
@Getter
@Setter
@NoArgsConstructor

public class User  {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    private Role role;
    private Location location;
    private List<Appointment> bookAppointments;

}
