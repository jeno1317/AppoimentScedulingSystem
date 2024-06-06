package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapStructConfig.class)
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDto locationToLocationDTO(Location location);
    Location locationDTOToLocation(LocationDto locationDTO);

}
