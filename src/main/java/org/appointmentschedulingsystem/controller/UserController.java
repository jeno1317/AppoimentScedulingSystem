package org.appointmentschedulingsystem.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.AppointmentDto;
import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.services.AppointmentService;
import org.appointmentschedulingsystem.services.UserService;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.exception.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Validated
@RequestMapping("/user")
@AllArgsConstructor

public class UserController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    @PostMapping("/user-add")
    public ResponseEntity<UserDto> userAdd(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @GetMapping("/get-user")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUserDetail());
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<List<UserDto>> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserDetail(id));
    }

    @PatchMapping("/user-update/{email}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("email") String email, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body( userService.userUpdate(email, userDto));
    }

    @DeleteMapping("/user-delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        boolean deleteUser = userService.deleteUser(id);
        if (deleteUser) {
            return ResponseEntity.ok("deleted user successfully");
        } else {
            throw new Exception("User not deleted !!");
        }
    }

    @PostMapping("/add-location/{id}")
    public ResponseEntity<LocationDto> addLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.addLocation(id, locationDTO));
    }

        @PatchMapping("/update-location/{id}")
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
            throw new Exception("User location not deleted !!");
        }

    }

        @PostMapping("/book-appointment/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @Valid @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.ok(appointmentService.bookAppointment(uid, sid, appointmentDTO));
    }

    @DeleteMapping("/delete-appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id) {

        boolean removeAppointment = appointmentService.removeAppointment(id);
        if (removeAppointment) {
            return ResponseEntity.ok("Appointment deleted successfully");

        } else {
            return ResponseEntity.badRequest().body("Appointment Not Deleted");
        }
    }

    @PatchMapping("/update-appointment/{id}/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable("id") String id,
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appointmentService.updateAppointment(id, uid, sid, appointmentDTO));
    }

    @GetMapping("/all-appointment")
    public ResponseEntity<List<AppointmentDto>> getAllAppointment() {
        return ResponseEntity.ok(appointmentService.getAppointments());
    }

    @GetMapping("/get-near-service-provider/{email}/{professionType}/{maxDistance}")
    public ResponseEntity<List<ServiceProviderDto>> searchNearServiceProvider(
            @PathVariable("email") String email,
            @PathVariable("professionType") ProfessionType professionType,
            @PathVariable("maxDistance") int maxDistance
    ) {
        return ResponseEntity.ok(userService.findNearbyServiceProviders(email, professionType, maxDistance));
    }

    @GetMapping("/profession-name-by-service-provider/{professionName}")
    public ResponseEntity<List<ServiceProviderDto>> searchByProfession(
            @PathVariable("professionName") ProfessionType professionName
    ) {
        return ResponseEntity.ok(userService.searchByProfession(professionName));
    }

}