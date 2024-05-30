package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper

public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User MapUserToUser(UserDto userDTO);
    UserDto UserToUserDTO(User User);

}
