package org.appointmentschedulingsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appointmentschedulingsystem.util.enums.HolidayType;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OffDay {

    private Date fromDate;
    private Date toDate;
    private HolidayType type;

}