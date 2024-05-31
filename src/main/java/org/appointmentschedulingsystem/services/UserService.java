package org.appointmentschedulingsystem.services;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.entity.Appointment;
import org.appointmentschedulingsystem.entity.Location;
import org.appointmentschedulingsystem.entity.ServiceProvider;
import org.appointmentschedulingsystem.entity.User;
import org.appointmentschedulingsystem.mapper.LocationMapper;
import org.appointmentschedulingsystem.mapper.ServiceProviderMapper;
import org.appointmentschedulingsystem.mapper.UserMapper;
import org.appointmentschedulingsystem.repositories.UserRepository;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.enums.Role;
import org.appointmentschedulingsystem.util.exception.Exception;
import org.appointmentschedulingsystem.util.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.appointmentschedulingsystem.repositories.ServiceProviderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Service
@AllArgsConstructor

public class UserService extends UserValidation {

    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUserDetail(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User Not Found"));
        List<Appointment> appointments = user.getBookAppointments();
        for (Appointment appointment : appointments) {
            appointment.setUid(user.getId());
        }
        Optional<User> user1 = userRepository.findById(id);
        return user1.stream().map(UserMapper.INSTANCE::UserToUserDTO).toList();
    }

    public List<UserDto> getAllUserDetail() {
        List<User> all = userRepository.findAll();
        if (all.isEmpty()) {
            throw new Exception("User detail not found !!");
        }
        return all.stream().map(UserMapper.INSTANCE::UserToUserDTO).toList();
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.MapUserToUser(userDto);
        User checkExistingUser = userRepository.findByEmail(user.getEmail());
        if (checkExistingUser != null) {
            throw new Exception("User already exists");
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userValidation(user);
        User save = userRepository.save(user);
        List<Appointment> appointments = save.getBookAppointments();
        for (Appointment appointment : appointments) {
            appointment.setUid(save.getId());
            userRepository.save(save);
        }
        return UserMapper.INSTANCE.UserToUserDTO(user);
    }

    public UserDto userUpdate(String email, UserDto userDto) {
        User user = UserMapper.INSTANCE.MapUserToUser(userDto);
        User byEmail = userRepository.findByEmail(email);
        if (email == null) {
            throw new Exception("User not found !!");
        }
        User update = UserMapper.INSTANCE.update(userDto, byEmail);
        user.setRole(Role.USER);
        userValidation(user);
        userRepository.save(update);
        return UserMapper.INSTANCE.UserToUserDTO(user);
    }

    public boolean deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User Not Found"));
        List<Appointment> appointments = user.getBookAppointments();
        for (Appointment appointment : appointments) {
            ServiceProvider serviceProvider = serviceProviderRepository.findById(appointment.getSid())
                    .orElseThrow(() -> new Exception("Service Provider Not Found"));
            List<Appointment> serviceAppointments = serviceProvider.getAppointments();
            serviceAppointments.removeIf(appointment1 -> appointment1.getId().equals(appointment.getId()));
            serviceProviderRepository.save(serviceProvider);
        }
        userRepository.deleteById(id);
        return true;
    }

    public LocationDto addLocation(String id, LocationDto locationDTO) {
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDTO);
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exist"));
        user.setLocation(location);
        userRepository.save(user);
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    public LocationDto updateLocation(String id, LocationDto locationDto) {
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDto);
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exist"));
        if (user.getId().equals(id)) {
            location.setCoordinates(location.getCoordinates());
            user.setLocation(location);
            userRepository.save(user);
        }
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    public boolean deleteLocation(String id, String type) {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("User does not exist"));
        Location location = user.getLocation();
        if (location != null && location.getType().equals(type)) {
            location.setCoordinates(Arrays.asList(0.0, 0.0));
        }
        userRepository.save(user);
        return true;
    }

    public List<ServiceProviderDto> findNearbyServiceProviders(
            String email,
            ProfessionType professionType,
            int maxDistance
    ) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User Not Found !!");
        }
        Location location = user.getLocation();
        List<Double> coordinates = location.getCoordinates();
        List<ServiceProvider> nearbyServiceProviders = serviceProviderRepository
                .findByLocationNear(coordinates, maxDistance);
        return nearbyServiceProviders.stream()
                .filter(serviceProvider -> professionType.equals(serviceProvider.getProfessionName()))
                .map(ServiceProviderMapper.INSTANCE::mapToDTO)
                .collect(Collectors.toList());
    }

    public  List<ServiceProviderDto> searchByProfession(ProfessionType professionName) {
        List<ServiceProvider> serviceProviders =serviceProviderRepository.findByProfessionName(professionName);
        if (serviceProviders.isEmpty()) {
            throw new Exception("Record not found for service Provider !!");
        }
        return serviceProviders.stream().map(ServiceProviderMapper.INSTANCE::mapToDTO).toList();
    }

}