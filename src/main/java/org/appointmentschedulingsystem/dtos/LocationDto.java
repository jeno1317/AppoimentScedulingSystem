package org.appointmentschedulingsystem.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class LocationDto {

    private String type;
    private List<Double> coordinates;

}
