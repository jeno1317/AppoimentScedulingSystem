package org.appointmentschedulingsystem.services;

import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    List<UserDto> getUserDetail(String id);
    List<UserDto> getAllUserDetail();
    UserDto addUser(UserDto userDto, MultipartFile image) throws IOException;
    UserDto userUpdate(String email, UserDto userDto);
    void deleteUser(String id);
    LocationDto addLocation(String id, LocationDto locationDTO);
    LocationDto updateLocation(String id, LocationDto locationDto);
    void deleteLocation(String id, String type);
    List<ServiceProviderDto> findNearbyServiceProviders(
            String email,
            ProfessionType professionType,
            int maxDistance
    );
    List<ServiceProviderDto> searchByProfession(ProfessionType professionName);

}
