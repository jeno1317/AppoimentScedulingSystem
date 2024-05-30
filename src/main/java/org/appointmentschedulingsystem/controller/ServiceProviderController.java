package org.appointmentschedulingsystem.controller;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.*;
import org.appointmentschedulingsystem.services.ServiceProviderService;
import org.appointmentschedulingsystem.util.enums.HolidayType;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.appointmentschedulingsystem.util.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.appointmentschedulingsystem.services.AppointmentService;
import java.util.List;
import java.util.Optional;

@RequestMapping("/ServiceProvider")
@RestController
@AllArgsConstructor

public class ServiceProviderController {

    private final ServiceProviderService ServiceProviderService;
    private final AppointmentService AppointmentService;

    @GetMapping("/professional")
    public ResponseEntity<List<ServiceProviderDto>> getDetailsOfProfession() {
        return ResponseEntity.ok(ServiceProviderService.getProfessional());
    }

    @GetMapping("/professional-by-id/{id}")
    public ResponseEntity<List<ServiceProviderDto>> getDetailsOfProfessionalById(@PathVariable("id") String id) {
        return ResponseEntity.ok(ServiceProviderService.serviceProviderById(id));
    }

    @PostMapping("/addServiceProvider")
    public ResponseEntity<ServiceProviderDto> addProfessionDetail(@RequestBody ServiceProviderDto serviceProviderDTO) {
        return ResponseEntity.of(Optional.of(ServiceProviderService.addServiceProvider(serviceProviderDTO)));
    }

    @DeleteMapping("/professional/{id}")
    public ResponseEntity<String> deleteProfession(@PathVariable("id") String id) {
        ServiceProviderService.deleteProfession(id);
        return ResponseEntity.ok("Service Provider deleted");
    }

    @PutMapping("/professional/{id}")
    public ResponseEntity<ServiceProviderDto> updateProfession(
            @PathVariable("id") String id,
            @RequestBody ServiceProviderDto serviceProviderDTO
    ) {
        return ResponseEntity.ok().body(ServiceProviderService.updateServiceProviderDetails(id, serviceProviderDTO));
    }

    @GetMapping("/professional/{professionName}")
    public ResponseEntity<List<ServiceProviderDto>> searchByProfession(
            @PathVariable("professionName") ProfessionType professionName
    ) {
        return ResponseEntity.ok(ServiceProviderService.searchByProfession(professionName));
    }

    @PostMapping("/professional/addDayAndTime/{id}")
    public ResponseEntity<List<AvailableWeekDayDto>> addWeekdays(
            @PathVariable("id") String id,
            @RequestBody List<AvailableWeekDayDto> availableWeekDayDTOS
    ) {
        return ResponseEntity.ok(ServiceProviderService.addDayAndTime(id, availableWeekDayDTOS));
    }

    @PostMapping("/professional/single-addDayAndTime/{id}")
    public ResponseEntity<AvailableWeekDayDto> addSingleTime(
            @PathVariable("id") String id,
            @RequestBody AvailableWeekDayDto availableWeekDay
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                ServiceProviderService.addSingleAvailableWeekDay(id, availableWeekDay)
        );
    }

    @DeleteMapping("/professional/delete-day/{id}/{day}")
    public ResponseEntity<String> deleteDayAndTime(@PathVariable("id") String id, @PathVariable("day") String day) {
        try {
            ServiceProviderService.deleteDayAndTime(id, day);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Delete day and time successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Delete day and time failed");
        }
    }

    @PutMapping("/professional/Day-toTime-FromTime/{id}")
    public ResponseEntity<AvailableWeekDayDto> updateDayAndTime(
            @PathVariable("id") String id,
            @RequestBody AvailableWeekDayDto availableWeekDayDTO
    ) {
        return ResponseEntity.ok().body(ServiceProviderService.updateDayAndTime(id, availableWeekDayDTO));
    }

    @GetMapping("/professional/id-time/{id}")
    public ResponseEntity<List<AvailableWeekDayDto>> getAllDayAndTime(@PathVariable("id") String id) {
        List<AvailableWeekDayDto> list = ServiceProviderService.getAvailableWeekDayDetail(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/professional/add-id-offDay/{id}")
    public ResponseEntity<List<OffDayDto>> addOfDay(
            @PathVariable("id") String id,
            @RequestBody List<OffDayDto> offDayDto
    ) {
        return ResponseEntity.ok(ServiceProviderService.addOffDay(id, offDayDto));
    }

    @PostMapping("/professional/add-single-offDay/{id}")
    public ResponseEntity<OffDayDto> addSingleDay(@PathVariable("id") String id, @RequestBody OffDayDto offDayDTO) {
        return ResponseEntity.ok(ServiceProviderService.addOffDay(id, offDayDTO));
    }

    @PutMapping("/professional/update-single-offDay/{id}")
    public ResponseEntity<OffDayDto> updateOffDay(@PathVariable("id") String id, @RequestBody OffDayDto offDay) {
        OffDayDto offDayDTO = ServiceProviderService.updateOffDay(id, offDay);
        return ResponseEntity.ok().body(offDayDTO);
    }

    @PutMapping("/professional/update-list-offDay/{id}")
    public ResponseEntity<List<OffDayDto>> updateOfDayList(
            @PathVariable("id") String id,
            @RequestBody List<OffDayDto> offDays
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ServiceProviderService.updateOffDay(id, offDays));
    }

    @DeleteMapping("/professional/delete-single-offDay/{id}/{type}")
    public ResponseEntity<String> deleteOffDay(@PathVariable("id") String id, @PathVariable("type") HolidayType type) {
        try {
            ServiceProviderService.deleteOffDay(id, type);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Delete the off day");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not delete the off day");
        }
    }

    @GetMapping("/professional/get-holiday/{id}")
    public ResponseEntity<List<OffDayDto>> getAllHolidayDetails(@PathVariable("id") String id) {
        return ResponseEntity.ok(ServiceProviderService.getOffDayDetails(id));
    }

    @PostMapping("/professional/add-location/{id}")
    public ResponseEntity<LocationDto> addLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderService.addProfessionalLocation(id, locationDTO));
    }

    @PutMapping("/professional/update-location/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderService.updateProfessionLocation(id, locationDTO));
    }

    @DeleteMapping("/professional/delete-location/{id}/{type}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") String id, @PathVariable("type") String type) {
        try {
            ServiceProviderService.deleteLocation(id, type);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("service Provider location deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service Provider location not deleted");
        }
    }

    @GetMapping("/professional/{longitude}/{latitude}/{maxDistanceInKm}")
    public ResponseEntity<List<ServiceProviderDto>> findByLocationNear(
            @PathVariable(value = "longitude") double longitude,
            @PathVariable(value = "latitude") double latitude,
            @PathVariable(value = "maxDistanceInKm") double maxDistanceInKm
    ) {
        List<ServiceProviderDto> byLocationNear = ServiceProviderService.getServiceProviders(
                longitude,
                latitude,
                maxDistanceInKm
        );
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(byLocationNear);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/professional/add-break-time/{id}/{day}")
    public ResponseEntity<List<BreakTimeDto>> addBreakTime(
            @PathVariable("id") String id,
            @PathVariable("day") String day,
            @RequestBody List<BreakTimeDto> breakTimeDTOS
    ) {
        try {
            List<BreakTimeDto> breakTimeDTOS1 = ServiceProviderService.addBreakTime(id, day, breakTimeDTOS);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(breakTimeDTOS1);

        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/professional/delete-break-time/{id}/{title}/{day}")
    public ResponseEntity<String> deleteBreakTime1(
            @PathVariable("id") String id,
            @PathVariable("title") String title,
            @PathVariable("day") String day
    ) {
        try {
            ServiceProviderService.deleteBreakTimes(id, title, day);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Break time deleted");

        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Break time not deleted");
        }
    }

    @PutMapping("/professional/update-break-time/{id}/{day}")
    public ResponseEntity<List<BreakTimeDto>> updateBreakTime(
            @PathVariable("id") String id,
            @PathVariable("day") String day,
            @RequestBody List<BreakTimeDto> breakTime
    ) {
        try {
            List<BreakTimeDto> breakTimeDTOS = ServiceProviderService.updateBreakTime(id, day, breakTime);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(breakTimeDTOS);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/professional/Update-appointment-status/{id}/{uid}/{sid}")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable("id") String id,
            @PathVariable("uid") String uid,
            @PathVariable("sid") String sid,
            @RequestBody AppointmentDto appointment
    ) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(AppointmentService.appointmentStatus(id, uid, sid, appointment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all-Appointment-Detail-by-id/{id}")
    public ResponseEntity<List<AppointmentDto>> getAllAppointment(@PathVariable("id") String id) {
        try {
            List<AppointmentDto> appointments = AppointmentService.getAppointments(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(appointments);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}