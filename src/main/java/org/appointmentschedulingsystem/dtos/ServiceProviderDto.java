package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.enums.Role;
import org.springframework.data.annotation.Id;
import java.util.List;

@Getter
@Setter

public class ServiceProviderDto {

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
    private List<AvailableWeekDayDto> availableWeekDay;
    private List<OffDayDto> offDay;
    private LocationDto location;
    private List<AppointmentDto> appointments;

}