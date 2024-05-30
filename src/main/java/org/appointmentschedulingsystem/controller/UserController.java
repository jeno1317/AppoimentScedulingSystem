package org.appointmentschedulingsystem.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.AppointmentDto;
import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.services.AppointmentService;
import org.appointmentschedulingsystem.services.ServiceProviderService;
import org.appointmentschedulingsystem.services.UserService;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
@RequestMapping("/User")
@AllArgsConstructor

public class UserController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final ServiceProviderService serviceProviderService;

    @PostMapping("/userAdd")
    public ResponseEntity<UserDto> userAdd(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @GetMapping("/getUser")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUserDetail());
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<List<UserDto>> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserDetail(id));
    }

    @PutMapping("/user-update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body( userService.userUpdate(id, userDto));
    }

    @DeleteMapping("/user-delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        boolean deleteUser = userService.deleteUser(id);
        if (deleteUser) {
            return ResponseEntity.ok("deleted user successfully");
        } else {
            throw new CustomException("User not deleted !!");
        }
    }

    @PostMapping("/addLocation/{id}")
    public ResponseEntity<LocationDto> addLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.addLocation(id, locationDTO));
    }

    @PutMapping("/update-location/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateLocation(id, locationDTO));
    }

    @DeleteMapping("/delete-location/{id}/{type}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") String id, @PathVariable("type") String type) {
        boolean deleteLocation = userService.deleteLocation(id, type);
        if (deleteLocation) {
            return ResponseEntity.ok("Deleted location");

        } else {
            throw new CustomException("User location not deleted !!");
        }

    }

    @PostMapping("/book-Appointment/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @Valid @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.ok(appointmentService.bookAppointment(uid, sid, appointmentDTO));
    }

    @DeleteMapping("/delete-Appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id) {

        boolean removeAppointment = appointmentService.removeAppointment(id);
        if (removeAppointment) {
            return ResponseEntity.ok("Appointment deleted successfully");

        } else {
            return ResponseEntity.badRequest().body("Appointment Not Deleted");
        }
    }

    @PutMapping("/update-Appointment/{id}/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable("id") String id,
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appointmentService.updateAppointment(id, uid, sid, appointmentDTO));
    }

    @GetMapping("/All-Appointment/")
    public ResponseEntity<List<AppointmentDto>> getAllAppointment() {
        return ResponseEntity.ok(appointmentService.getAppointments());
    }

    @GetMapping("/Get-nearServiceProvider/{id}/{professionType}/{maxDistance}/")
    public ResponseEntity<List<ServiceProviderDto>> searchNearServiceProvider(
            @PathVariable("id") String id,
            @PathVariable("professionType") ProfessionType professionType,
            @PathVariable("maxDistance") int maxDistance
    ) {
        return ResponseEntity.ok(userService.findNearbyServiceProviders(id, professionType, maxDistance));
    }

}