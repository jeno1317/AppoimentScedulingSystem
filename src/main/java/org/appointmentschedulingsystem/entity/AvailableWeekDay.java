package org.appointmentschedulingsystem.entity;

import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.config.LocalDateTimeConverter;
import org.springframework.data.convert.ValueConverter;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AvailableWeekDay {

    private String day;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime fromTime;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime toTime;

    private List<BreakTime> breakTime;

}

