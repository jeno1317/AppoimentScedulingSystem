package org.appointmentschedulingsystem.services;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.AppointmentDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.repositories.AppointmentRepository;
import org.appointmentschedulingsystem.repositories.UserRepository;
import org.appointmentschedulingsystem.entity.Appointment;
import org.appointmentschedulingsystem.entity.ServiceProvider;
import org.appointmentschedulingsystem.entity.User;
import org.appointmentschedulingsystem.mapper.AppointmentMapper;
import org.appointmentschedulingsystem.mapper.ServiceProviderMapper;
import org.appointmentschedulingsystem.mapper.UserMapper;
import org.appointmentschedulingsystem.repositories.ServiceProviderRepository;
import org.appointmentschedulingsystem.util.enums.BookStatus;
import org.appointmentschedulingsystem.util.exception.Exception;
import org.appointmentschedulingsystem.util.validation.GlobalValidation;
import org.springframework.stereotype.Component;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor

public class AppointmentService {

    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentDto bookAppointment(String uid, String sid, AppointmentDto appointmentDTO) {
        Appointment appointment = AppointmentMapper.INSTANCE.appointmentToAppointment(appointmentDTO);

        User user = userRepository.findById(uid).orElseThrow(() -> new Exception("User Not Found"));
        ServiceProvider serviceProvider = serviceProviderRepository.
                findById(sid).orElseThrow(() -> new Exception("Service Provider Not Found"));

        if (user.getBookAppointments() == null) {
            user.setBookAppointments(new ArrayList<>());
        }
        if (serviceProvider.getAppointments() == null) {
            serviceProvider.setAppointments(new ArrayList<>());
        }

        appointment.setSid(sid);
        appointment.setUid(uid);
        appointment.setStatus(BookStatus.PENDING);
        LocalDate appointmentDate = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();

        boolean isBooked = user.getBookAppointments().stream()
                .anyMatch(a -> isSameAppointment(a, appointment, appointmentDayOfWeek));

        if (isBooked) {
            throw new Exception("Appointment Already Booked");
        }
        GlobalValidation.validateAppointment(serviceProvider, appointment);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        user.getBookAppointments().add(savedAppointment);
        serviceProvider.getAppointments().add(savedAppointment);
        userRepository.save(user);
        serviceProviderRepository.save(serviceProvider);
        return AppointmentMapper.INSTANCE.appointmentToAppointmentDTO(savedAppointment);
    }

    private boolean isSameAppointment(Appointment a, Appointment appointment, DayOfWeek appointmentDayOfWeek) {
        return a.getDate().equals(appointment.getDate()) &&
                a.getDay().equals(appointmentDayOfWeek.toString()) &&
                a.getFromTime().equals(appointment.getFromTime()) &&
                a.getToTime().equals(appointment.getToTime());
    }

    public void removeAppointment(String id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment Not Found"));
        User user = userRepository.findById(appointment.getUid())
                .orElseThrow(() -> new Exception("User Not Found"));
        ServiceProvider serviceProvider = serviceProviderRepository.findById(appointment.getSid())
                .orElseThrow(() -> new Exception("Service Provider Not Found"));

        UserDto userDTO = UserMapper.INSTANCE.UserToUserDTO(user);
        ServiceProviderDto serviceProviderDTO = ServiceProviderMapper.INSTANCE.mapToDTO(serviceProvider);

        ZoneId zone = ZoneId.systemDefault();
        LocalTime currentTime = LocalTime.now(zone);
        LocalTime appointmentTime = appointment.getFromTime();
        Duration timeDifference = Duration.between(currentTime, appointmentTime);
        long hoursUntilAppointment = timeDifference.toHours();

        if (hoursUntilAppointment >= 2) {
            userDTO.getBookAppointments().removeIf(appointmentDTO -> appointmentDTO.getId().equals(id));
            serviceProviderDTO.getAppointments().removeIf(a -> a.getId().equals(id));
            User updatedUser = UserMapper.INSTANCE.MapUserToUser(userDTO);
            ServiceProvider updatedServiceProvider = ServiceProviderMapper.INSTANCE.mapToEntity(serviceProviderDTO);
            userRepository.save(updatedUser);
            serviceProviderRepository.save(updatedServiceProvider);
            appointmentRepository.deleteById(id);
        } else {
            throw new Exception("You cannot delete this appointment because it is too close to the scheduled time.");
        }
    }

    public AppointmentDto updateAppointment(String id, AppointmentDto appointmentDTO) {
        Appointment updatedAppointment = AppointmentMapper.INSTANCE.appointmentToAppointment(appointmentDTO);

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment Not Found"));
        ServiceProvider serviceProvider = serviceProviderRepository.findById(existingAppointment.getSid())
                .orElseThrow(() -> new Exception("sid not found"));
        User user = userRepository.findById(existingAppointment.getUid())
                .orElseThrow(() -> new Exception("User Not Found"));

        existingAppointment.setDate(updatedAppointment.getDate());
        existingAppointment.setDay(updatedAppointment.getDay());
        existingAppointment.setFromTime(updatedAppointment.getFromTime());
        existingAppointment.setToTime(updatedAppointment.getToTime());

        GlobalValidation.validateAppointment(serviceProvider, updatedAppointment);
        Appointment savedAppointment = appointmentRepository.save(existingAppointment);
        updateUserAppointment(user, savedAppointment);
        updateServiceProviderAppointment(serviceProvider, savedAppointment);
        return AppointmentMapper.INSTANCE.appointmentToAppointmentDTO(updatedAppointment);
    }

    public void updateUserAppointment(User user, Appointment appointment) {
        List<Appointment> userBookedAppointments = user.getBookAppointments();
        boolean appointmentUpdated = false;
        for (int i = 0; i < userBookedAppointments.size(); i++) {
            Appointment userAppointment = userBookedAppointments.get(i);
            if (userAppointment.getId().equals(appointment.getId())) {
                userBookedAppointments.set(i, appointment);
                appointmentUpdated = true;
                break;
            }
        }
        if (!appointmentUpdated) {
            throw new IllegalArgumentException("Appointment not found for the user");
        }
        userRepository.save(user);
    }

    public void updateServiceProviderAppointment(ServiceProvider serviceProvider, Appointment appointment) {
        List<Appointment> serviceProviderBookedAppointments = serviceProvider.getAppointments();
        for (int i = 0; i < serviceProviderBookedAppointments.size(); i++) {
            Appointment serviceProviderAppointment = serviceProviderBookedAppointments.get(i);
            if (serviceProviderAppointment.getId().equals(appointment.getId())) {
                serviceProviderBookedAppointments.set(i, appointment);
                break;
            }
        }
        serviceProviderRepository.save(serviceProvider);
    }

    public List<AppointmentDto> getAppointments() {
        List<Appointment> allAppointment = appointmentRepository.findAll();
        if (allAppointment.isEmpty()) {
            throw new Exception("Appointment NOT Found !!");
        }
        return allAppointment.stream().map(AppointmentMapper.INSTANCE::appointmentToAppointmentDTO).toList();
    }

    public AppointmentDto appointmentStatus(String id, AppointmentDto appointmentDTO) {
        Appointment appointment = AppointmentMapper.INSTANCE.appointmentToAppointment(appointmentDTO);

        Appointment appointment1 = appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment Not Found"));
        ServiceProvider serviceProvider = serviceProviderRepository.findById(appointment1.getSid())
                .orElseThrow(() -> new Exception("Service Provider Not Found"));
        User user = userRepository.findById(appointment1.getUid())
                .orElseThrow(() -> new Exception("User Not Found"));

        appointment1.setStatus(appointment.getStatus());
        Appointment save = appointmentRepository.save(appointment1);
        updateUserAppointment(user, save);
        updateServiceProviderAppointment(serviceProvider, save);
        return AppointmentMapper.INSTANCE.appointmentToAppointmentDTO(appointment1);
    }

    public List<AppointmentDto> getAppointments(String id) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new Exception("Service Provider Not Found"));
        List<Appointment> userBookedAppointments = serviceProvider.getAppointments();
        return AppointmentMapper.INSTANCE.appointmentsToAppointmentDTO(userBookedAppointments);
    }

}