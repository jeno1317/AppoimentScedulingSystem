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
import org.appointmentschedulingsystem.util.exception.CustomException;
import org.appointmentschedulingsystem.util.validation.UserValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.appointmentschedulingsystem.repositories.ServiceProviderRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Service
@AllArgsConstructor

public class UserService extends UserValidation {

    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getUserDetail(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User Not Found"));
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
            throw new CustomException("User detail not found !!");
        }
        return all.stream().map(UserMapper.INSTANCE::UserToUserDTO).toList();
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.INSTANCE.MapUserToUser(userDto);
        User checkExistingUser = userRepository.findByEmail(user.getEmail());
        if (checkExistingUser != null) {
            throw new CustomException("User already exists");
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

    public UserDto userUpdate(String id, UserDto userDto) {
        User user = UserMapper.INSTANCE.MapUserToUser(userDto);

        if (user.getId().equals(id)) {
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setEmail(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAddress(user.getAddress());
            user.setPhoneNumber(user.getPhoneNumber());
            user.setLocation(user.getLocation());
            user.setRole(Role.USER);
            userValidation(user);
            userRepository.save(user);
        } else {
            throw new CustomException("User not exists");
        }
        return UserMapper.INSTANCE.UserToUserDTO(user);
    }

    public boolean deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User Not Found"));
        List<Appointment> appointments = user.getBookAppointments();
        for (Appointment appointment : appointments) {
            ServiceProvider serviceProvider = serviceProviderRepository.findById(appointment.getSid())
                    .orElseThrow(() -> new CustomException("Service Provider Not Found"));
            List<Appointment> serviceAppointments = serviceProvider.getAppointments();
            serviceAppointments.removeIf(appointment1 -> appointment1.getId().equals(appointment.getId()));
            serviceProviderRepository.save(serviceProvider);
        }
        userRepository.deleteById(id);
        return true;
    }

    public LocationDto addLocation(String id, LocationDto locationDTO) {
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDTO);
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User does not exist"));
        user.setLocation(location);
        userRepository.save(user);
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    public LocationDto updateLocation(String id, LocationDto locationDto) {
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDto);
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User does not exist"));
        if (user.getId().equals(id)) {
            location.setCoordinates(location.getCoordinates());
            user.setLocation(location);
            userRepository.save(user);
        }
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    public boolean deleteLocation(String id, String type) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User does not exist"));
        Location location = user.getLocation();
        if (location != null && location.getType().equals(type)) {
            location.setCoordinates(Arrays.asList(0.0, 0.0));
        }
        userRepository.save(user);
        return true;
    }

    public List<ServiceProviderDto> findNearbyServiceProviders(
            String id,
            ProfessionType professionType,
            int maxDistance
    ) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User does not exist"));
        Location location = user.getLocation();
        List<Double> coordinates = location.getCoordinates();
        List<ServiceProvider> nearbyServiceProviders = serviceProviderRepository
                .findByLocationNear(coordinates, maxDistance);
        List<ServiceProvider> result = new ArrayList<>();
        for (ServiceProvider serviceProvider : nearbyServiceProviders) {
            if (serviceProvider.getProfessionName().equals(professionType)) {
                result.add(serviceProvider);
            }
        }
        return result.stream().map(ServiceProviderMapper.INSTANCE::mapToDTO).toList();
    }

}