package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import org.appointmentschedulingsystem.config.LocalDateTimeConverter;
import org.springframework.data.convert.ValueConverter;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter

public class AvailableWeekDayDto {

    private String day;
    private List<BreakTimeDto> breakTime;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime fromTime;

    @ValueConverter(LocalDateTimeConverter.class)
    private LocalTime toTime;

}
