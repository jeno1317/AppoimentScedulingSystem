package org.appointmentschedulingsystem.entity;

import lombok.Getter;
import lombok.Setter;

import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "appointmentschedulingsystem")
@Getter
@Setter
public class ServiceProvider {

    @Id
    private String id;
    private String professionalFirstName;
    private String professionalLastName;
    private ProfessionType professionName;
    private String professionContactNumber;
    private String email;
    private String password;
    private String professionAddress;
    private Role role;
    private List<AvailableWeekDay> availableWeekDay;
    private List<OffDay> offDay;
    private Location location;
    private List<Appointment> appointments;

}
