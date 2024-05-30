package org.appointmentschedulingsystem.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class Location {

    private String type;
    private List<Double> coordinates;

}
