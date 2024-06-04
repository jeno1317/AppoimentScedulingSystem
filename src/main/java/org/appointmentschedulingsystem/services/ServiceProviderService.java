package org.appointmentschedulingsystem.services;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.*;
import org.appointmentschedulingsystem.entity.*;
import org.appointmentschedulingsystem.mapper.*;
import org.appointmentschedulingsystem.mapper.ServiceProviderMapper;
import org.appointmentschedulingsystem.repositories.ServiceProviderRepository;
import org.appointmentschedulingsystem.util.enums.HolidayType;
import org.appointmentschedulingsystem.util.enums.Role;
import org.appointmentschedulingsystem.util.exception.Exception;
import org.appointmentschedulingsystem.util.validation.ServiceProviderValidation;
import org.appointmentschedulingsystem.util.validation.GlobalValidation;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.appointmentschedulingsystem.util.validation.ServiceProviderValidation.checkBreakTimes;
import static org.appointmentschedulingsystem.util.validation.ServiceProviderValidation.checkOffDay;

@Configuration
@Component
@Service
@AllArgsConstructor

public class ServiceProviderService {

    private final GlobalValidation validation;
    private final ServiceProviderRepository ServiceProviderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ServiceProviderDto> getProfessional() {
        List<ServiceProvider> all = ServiceProviderRepository.findAll();
        if (all.isEmpty()) {
            throw new Exception("Service Empty Provider found !! ");
        }
        return all.stream().map(ServiceProviderMapper.INSTANCE::mapToDTO).toList();
    }

    public ServiceProviderDto addServiceProvider(ServiceProviderDto serviceProviderDTO) {
        ServiceProvider serviceProvider1 = ServiceProviderRepository.findByEmail(serviceProviderDTO.getEmail());
        if (serviceProvider1 != null) {
            throw new Exception("ServiceProvider UserName Already Exists: " + serviceProviderDTO.getEmail());
        }
        ServiceProvider serviceProvider = ServiceProviderMapper.INSTANCE.mapToEntity(serviceProviderDTO);
        serviceProvider.setRole(Role.SERVICE_PROVIDER);
        serviceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));
        ServiceProviderValidation.serviceProviderValidation(serviceProvider);
        ServiceProviderValidation.checkAvailableWeekDay(serviceProvider.getAvailableWeekDay());
        checkBreakTimes(serviceProvider.getAvailableWeekDay());
        checkOffDay(serviceProvider.getOffDay());
        ServiceProvider save = ServiceProviderRepository.save(serviceProvider);
        return ServiceProviderMapper.INSTANCE.mapToDTO(save);
    }

    public List<ServiceProviderDto> serviceProviderById(String id) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service provider not found" + " : " + id));
        List<Appointment> appointments = serviceProvider.getAppointments();
        for (Appointment appointment : appointments) {
            appointment.setId(appointment.getId());
        }
        Optional<ServiceProvider> byId = ServiceProviderRepository.findById(id);
        return byId.stream().map(ServiceProviderMapper.INSTANCE::mapToDTO).toList();
    }

    public void deleteProfession(String id) {
        ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service provider not found" + " : " + id));
        ServiceProviderRepository.deleteById(id);
    }

    public ServiceProviderDto updateServiceProviderDetails(String email, ServiceProviderDto serviceProviderDTO) {
        ServiceProvider serviceProvider = ServiceProviderMapper.INSTANCE.mapToEntity(serviceProviderDTO);
        ServiceProvider serviceProvider1 = ServiceProviderRepository.findByEmail(email);
        ServiceProvider serviceProviderDemo = ServiceProviderMapper.INSTANCE.update(serviceProviderDTO, serviceProvider1);
        ServiceProviderValidation.serviceProviderValidation(serviceProvider);
        ServiceProviderValidation.checkAvailableWeekDay(serviceProvider.getAvailableWeekDay());
        checkBreakTimes(serviceProvider.getAvailableWeekDay());
        checkOffDay(serviceProvider.getOffDay());
        ServiceProviderRepository.save(serviceProviderDemo);
        return ServiceProviderMapper.INSTANCE.mapToDTO(serviceProviderDemo);
    }

    public AvailableWeekDayDto addSingleAvailableWeekDay(String id, AvailableWeekDayDto availableWeekDayDTO) {
        AvailableWeekDay availableWeekDay = AvailableWeekDayMapper.INSTANCE.DTOToMap(availableWeekDayDTO);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Professional Not Found"));
        List<AvailableWeekDay> existingWeekDays = serviceProvider.getAvailableWeekDay();
        existingWeekDays.sort(Comparator.comparingInt(day -> getDayOfWeekNumber(day.getDay())));
        boolean matchFound = existingWeekDays.stream()
                .anyMatch(day -> day.getDay().equalsIgnoreCase(availableWeekDay.getDay()));
        if (matchFound) {
            existingWeekDays
                    .stream()
                    .filter(day -> day.getDay().equalsIgnoreCase(availableWeekDay.getDay()))
                       .findFirst().ifPresent(day -> {
                        day.setFromTime(availableWeekDay.getFromTime());
                        day.setToTime(availableWeekDay.getToTime());
                        day.setBreakTime(availableWeekDay.getBreakTime());
                    });
        } else {
            existingWeekDays.add(availableWeekDay);
        }
        ServiceProviderRepository.save(serviceProvider);
        return AvailableWeekDayMapper.INSTANCE.mapToDTO(availableWeekDay);
    }

    private int getDayOfWeekNumber(String day) {
        return switch (day.toUpperCase()) {
            case "MONDAY" -> 1;
            case "TUESDAY" -> 2;
            case "WEDNESDAY" -> 3;
            case "THURSDAY" -> 4;
            case "FRIDAY" -> 5;
            case "SATURDAY" -> 6;
            case "SUNDAY" -> 7;
            default -> throw new Exception("Invalid day of the week: " + day);
        };
    }

    public List<AvailableWeekDayDto> addDayAndTime(String id, List<AvailableWeekDayDto> availableWeekDayDTOS) {
        List<AvailableWeekDay> availableWeekDays = AvailableWeekDayMapper.INSTANCE.mapToList(availableWeekDayDTOS);
        availableWeekDayDTOS.forEach(availableWeekDay -> addSingleAvailableWeekDay(id, availableWeekDay));
        return AvailableWeekDayMapper.INSTANCE.mapToDTO(availableWeekDays);
    }

    public void deleteDayAndTime(String id, String day) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Professional Not Found"));
        List<AvailableWeekDay> availableWeekDay = serviceProvider.getAvailableWeekDay();
        if (availableWeekDay.isEmpty()) {
            throw new Exception("Service Provider Not Found AvailableWeekDay !! ");
        } else {
            availableWeekDay.removeIf(WeekDay -> WeekDay.getDay().equalsIgnoreCase(day));
            ServiceProviderRepository.save(serviceProvider);
        }
    }

    public AvailableWeekDayDto updateDayAndTime(String id, AvailableWeekDayDto availableWeekDayDTO) {
        AvailableWeekDay availableWeekDay = AvailableWeekDayMapper.INSTANCE.DTOToMap(availableWeekDayDTO);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Professional Not Found"));
        List<AvailableWeekDay> availableWeekDays = serviceProvider.getAvailableWeekDay();
        for (AvailableWeekDay Day : availableWeekDays) {
            if (Day.getDay().equalsIgnoreCase(availableWeekDay.getDay())) {
                Day.setToTime(availableWeekDay.getToTime());
                Day.setFromTime(availableWeekDay.getFromTime());
                Day.setBreakTime(availableWeekDay.getBreakTime());
                Day.setDay(availableWeekDay.getDay());
                break;
            }
        }
        ServiceProviderRepository.save(serviceProvider);
        return AvailableWeekDayMapper.INSTANCE.mapToDTO(availableWeekDay);
    }

    //  getAllDayAndTime detail
    public List<AvailableWeekDayDto> getAvailableWeekDayDetail(String id) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Professional Not Found"));
        List<AvailableWeekDay> availableWeekDay = serviceProvider.getAvailableWeekDay();
        if (availableWeekDay.isEmpty()) {
            throw new Exception("Service Provider not Available !!");
        }
        return availableWeekDay.stream().map(AvailableWeekDayMapper.INSTANCE::mapToDTO).toList();
    }

    //add offDaySingle
    public OffDayDto addOffDay(String id, OffDayDto offDayDTO) {
        OffDay offDayData = OffDayMapper.INSTANCE.OffDayDTOToOffDay(offDayDTO);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider not found"));
        boolean exist = serviceProvider.getOffDay()
                .stream()
                .anyMatch(offDay -> offDay.getType().equals(offDayData.getType()));

        if (exist) {
            serviceProvider.getOffDay().stream()
                    .filter(existingOffDay -> existingOffDay.getType().equals(offDayData.getType()))
                    .findFirst()
                    .ifPresent(existingOffDay -> {
                        existingOffDay.setFromDate(offDayData.getFromDate());
                        existingOffDay.setToDate(offDayData.getToDate());
                        validation.validateDate(existingOffDay);
                    });
        } else {
            serviceProvider.getOffDay().add(offDayData);
        }
        ServiceProviderRepository.save(serviceProvider);
        return OffDayMapper.INSTANCE.OffDayToOffDayDTO(offDayData);
    }

    // add offDayList
    public List<OffDayDto> addOffDay(String id, List<OffDayDto> offDayDto) {
        List<OffDay> list = OffDayMapper.INSTANCE.OffDayDTOsToOffDays(offDayDto);
        offDayDto.forEach(offDay -> addOffDay(id, offDay));
        return OffDayMapper.INSTANCE.OffDaysToOffDayDTOs(list);
    }

    public List<OffDayDto> updateOffDay(String id, List<OffDayDto> offDayDTOList) {
        List<OffDay> list = OffDayMapper.INSTANCE.OffDayDTOsToOffDays(offDayDTOList);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Id not found"));
        List<OffDay> existingOffDays = serviceProvider.getOffDay();
        for (OffDay updatedOffDay : list) {
            for (OffDay existingOffDay : existingOffDays) {
                if (existingOffDay.getType().equals(updatedOffDay.getType())) {
                    existingOffDay.setType(updatedOffDay.getType());
                    existingOffDay.setToDate(updatedOffDay.getToDate());
                    existingOffDay.setFromDate(updatedOffDay.getFromDate());
                    validation.validateDate(existingOffDay);
                    ServiceProviderRepository.save(serviceProvider);
                    break;
                }
            }
        }
        ServiceProviderRepository.save(serviceProvider);
        return OffDayMapper.INSTANCE.OffDaysToOffDayDTOs(list);
    }

    //update offDay
    public OffDayDto updateOffDay(String id, OffDayDto offDayDTO) {
        OffDay offDay = OffDayMapper.INSTANCE.OffDayDTOToOffDay(offDayDTO);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Id not found"));
        List<OffDay> offDays = serviceProvider.getOffDay();
        for (OffDay type : offDays) {
            if (type.getType().equals(offDay.getType())) {
                type.setFromDate(offDay.getFromDate());
                type.setToDate(offDay.getToDate());
                type.setType(offDay.getType());
                validation.validateDate(offDay);
                break;
            }
        }
        ServiceProviderRepository.save(serviceProvider);
        return OffDayMapper.INSTANCE.OffDayToOffDayDTO(offDay);
    }

    //delete of days
    public void deleteOffDay(String id, HolidayType type) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Id not found"));
        List<OffDay> offDay = serviceProvider.getOffDay();
        if (offDay.isEmpty()) {
            throw new Exception("Holiday Type is Not Found !!");
        }
        offDay.removeIf(Type -> Type.getType().equals(type));
        ServiceProviderRepository.save(serviceProvider);
    }

    // get all offDay details
    public List<OffDayDto> getOffDayDetails(String id) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Id not found"));
        List<OffDay> offDay = serviceProvider.getOffDay();
        if (offDay.isEmpty()) {
            throw new Exception("Service Provider OffDay NOT Record !!");
        }
        return offDay.stream().map(OffDayMapper.INSTANCE::OffDayToOffDayDTO).toList();
    }

    //add location
    public LocationDto addProfessionalLocation(String id, LocationDto locationDTO) {
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDTO);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider ID NOT Found"));
        location.setType(location.getType());
        location.setCoordinates(location.getCoordinates());
        serviceProvider.setLocation(location);
        ServiceProviderRepository.save(serviceProvider);
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    public LocationDto updateProfessionLocation(String id, LocationDto locationDTO) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider ID NOT Found"));
        Location location = LocationMapper.INSTANCE.locationDTOToLocation(locationDTO);
        if (serviceProvider.getLocation().equals(location)) {
            throw new Exception("Location is same as existing location.");
        }
        serviceProvider.setLocation(location);
        ServiceProviderRepository.save(serviceProvider);
        return LocationMapper.INSTANCE.locationToLocationDTO(location);
    }

    //add or update break time
    public List<BreakTimeDto> addBreakTime(String id, String day, List<BreakTimeDto> breakTimeDTO) {
        List<BreakTime> breakTimes = BreakTimeMapper.INSTANCE.breakTimeDTOListToBreakTimeList(breakTimeDTO);
        ServiceProvider professional = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider ID NOT Found"));
        List<AvailableWeekDay> availableWeekDays = professional.getAvailableWeekDay();
        Optional<AvailableWeekDay> optionalAvailableWeekDay = availableWeekDays.stream()
                .filter(availableWeekDay -> availableWeekDay.getDay().equalsIgnoreCase(day))
                .findFirst();
        if (optionalAvailableWeekDay.isPresent()) {
            AvailableWeekDay availableWeekDay = optionalAvailableWeekDay.get();
            availableWeekDay.setBreakTime(breakTimes);
            GlobalValidation.breakTimeCheckList(breakTimes);
        } else {
            AvailableWeekDay newAvailableWeekDay = new AvailableWeekDay();
            newAvailableWeekDay.setDay(day);
            newAvailableWeekDay.setBreakTime(breakTimes);
            availableWeekDays.add(newAvailableWeekDay);
            GlobalValidation.breakTimeCheck((BreakTime) breakTimes);
        }
        ServiceProviderRepository.save(professional);
        return BreakTimeMapper.INSTANCE.breakTimeListToBreakTimeDTOList(breakTimes);
    }

    //delete breakTime
    public void deleteBreakTimes(String id, String title, String day) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service provider with the given ID not found"));
        for (AvailableWeekDay availableWeekDay : serviceProvider.getAvailableWeekDay()) {
            if (availableWeekDay.getDay().equalsIgnoreCase(day)) {
                List<BreakTime> breakTimes = availableWeekDay.getBreakTime();
                Optional<BreakTime> breakTimeOptional = breakTimes
                        .stream()
                        .filter(breakTime -> breakTime.getTitle().equalsIgnoreCase(title))
                        .findFirst();
                breakTimeOptional.ifPresent(breakTimes::remove);
                break;
            }
        }
        ServiceProviderRepository.save(serviceProvider);
    }

    public List<BreakTimeDto> updateBreakTime(String id, String day, List<BreakTimeDto> breakTimeDTOS) {
        List<BreakTime> breakTimes = BreakTimeMapper.INSTANCE.breakTimeDTOListToBreakTimeList(breakTimeDTOS);
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider NOT Found"));
        List<AvailableWeekDay> availableWeekDays = serviceProvider.getAvailableWeekDay();
        AvailableWeekDay availableWeekDay = availableWeekDays
                .stream()
                .filter(availableWeekDay1 -> availableWeekDay1.getDay().equalsIgnoreCase(day))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No AvailableWeekDay found for the provided day."));
        availableWeekDay.setBreakTime(breakTimes);
        ServiceProviderRepository.save(serviceProvider);
        return BreakTimeMapper.INSTANCE.breakTimeListToBreakTimeDTOList(breakTimes);
    }

    public void deleteLocation(String id, String type) {
        ServiceProvider serviceProvider = ServiceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider NOT Found"));
        Location location = serviceProvider.getLocation();
        if (location != null && location.getType().equals(type)) {
            location.setCoordinates(Arrays.asList(0.0, 0.0));
        }
        ServiceProviderRepository.save(serviceProvider);
    }
}