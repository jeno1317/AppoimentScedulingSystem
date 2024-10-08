package org.appointmentschedulingsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.AppointmentDto;
import org.appointmentschedulingsystem.dtos.LocationDto;
import org.appointmentschedulingsystem.dtos.ServiceProviderDto;
import org.appointmentschedulingsystem.dtos.UserDto;
import org.appointmentschedulingsystem.services.AppointmentServiceImp;
import org.appointmentschedulingsystem.services.UserServiceImp;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserServiceImp userServiceImp;
    private final AppointmentServiceImp appointmentServiceImp;

    @PostMapping("/user-add")
    public ResponseEntity<UserDto> registerUser(
            @RequestPart("user") String userDtoString,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDto userDto = objectMapper.readValue(userDtoString, UserDto.class);
            return ResponseEntity.ok(userServiceImp.addUser(userDto, image));
    }

    @GetMapping("/get-user")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userServiceImp.getAllUserDetail());
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<List<UserDto>> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userServiceImp.getUserDetail(id));
    }

    @PatchMapping("/user-update/{email}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("email") String email, @RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userServiceImp.userUpdate(email, userDto));
    }

    @DeleteMapping("/user-delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userServiceImp.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-location/{id}")
    public ResponseEntity<LocationDto> addLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userServiceImp.addLocation(id, locationDTO));
    }

    @PatchMapping("/update-location/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userServiceImp.updateLocation(id, locationDTO));
    }

    @DeleteMapping("/delete-location/{id}/{type}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") String id, @PathVariable("type") String type) {
        userServiceImp.deleteLocation(id, type);
        return ResponseEntity.ok("Deleted location");
    }

    @PostMapping("/book-appointment/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.ok(appointmentServiceImp.bookAppointment(uid, sid, appointmentDTO));
    }

    @DeleteMapping("/delete-appointment/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("id") String id) {
        appointmentServiceImp.removeAppointment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-appointment/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable("id") String id,
            @RequestBody AppointmentDto appointmentDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(appointmentServiceImp.updateAppointment(id, appointmentDTO));
    }

    @GetMapping("/all-appointment")
    public ResponseEntity<List<AppointmentDto>> getAllAppointment() {
        return ResponseEntity.ok(appointmentServiceImp.getAppointments());
    }

    @GetMapping("/get-near-service-provider/{email}/{professionType}/{maxDistance}")
    public ResponseEntity<List<ServiceProviderDto>> searchNearServiceProvider(
            @PathVariable("email") String email,
            @PathVariable("professionType") ProfessionType professionType,
            @PathVariable("maxDistance") int maxDistance
    ) {
        return ResponseEntity.ok(userServiceImp.findNearbyServiceProviders(email, professionType, maxDistance));
    }

    @GetMapping("/profession-name-by-service-provider/{professionName}")
    public ResponseEntity<List<ServiceProviderDto>> searchByProfession(
            @PathVariable("professionName") ProfessionType professionName
    ) {
        return ResponseEntity.ok(userServiceImp.searchByProfession(professionName));
    }

}