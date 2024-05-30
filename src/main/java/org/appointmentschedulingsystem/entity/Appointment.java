package org.appointmentschedulingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.appointmentschedulingsystem.config.LocalDateTimeConverter;
import org.appointmentschedulingsystem.util.enums.BookStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalTime;
import java.util.Date;

@Document(collection = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Appointment {

    @Id
    private String id;
    private String day;
    private Date date;
    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime fromTime;
    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime toTime;
    private BookStatus status;
    private String uid;
    private String sid;

}
