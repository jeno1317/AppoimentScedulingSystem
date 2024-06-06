package org.appointmentschedulingsystem.config;

import org.mapstruct.Builder;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR,
        builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.DEFAULT
)
public class MapStructConfig {
}
