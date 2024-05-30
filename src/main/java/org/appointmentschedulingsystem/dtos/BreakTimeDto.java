package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.config.LocalDateTimeConverter;
import org.springframework.data.convert.ValueConverter;
import java.time.LocalTime;

@Getter
@Setter

public class BreakTimeDto {

    private String title;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime fromTime1;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime toTime1;

}
