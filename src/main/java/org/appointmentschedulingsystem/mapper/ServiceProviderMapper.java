package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.entity.ServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config =    MapStructConfig.class)
public interface ServiceProviderMapper {

    ServiceProviderMapper INSTANCE = Mappers.getMapper(ServiceProviderMapper.class);

    ServiceProviderDto mapToDTO(ServiceProvider serviceProvider);
    ServiceProvider mapToEntity(ServiceProviderDto serviceProviderDTO);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "password",ignore = true)
    ServiceProvider update( ServiceProviderDto dto, @MappingTarget ServiceProvider serviceProvider);

}
