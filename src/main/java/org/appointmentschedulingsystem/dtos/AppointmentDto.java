package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.util.enums.BookStatus;
import org.springframework.data.annotation.Id;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class AppointmentDto {

    @Id
    private String id;
    private String day;
    private Date date;
    private LocalTime fromTime;
    private LocalTime toTime;
    private BookStatus status;
    private String uid;
    private String sid;

}
