package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User MapUserToUser(UserDto userDTO);
    UserDto UserToUserDTO(User User);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "email",ignore = true)
    @Mapping(target = "password",ignore = true)
    User update(UserDto userDto, @MappingTarget User user);

}
