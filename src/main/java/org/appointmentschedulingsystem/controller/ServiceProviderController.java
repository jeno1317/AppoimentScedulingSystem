package org.appointmentschedulingsystem.controller;

import lombok.AllArgsConstructor;
import org.appointmentschedulingsystem.dtos.*;
import org.appointmentschedulingsystem.services.ServiceProviderServiceImp;
import org.appointmentschedulingsystem.util.enums.HolidayType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.appointmentschedulingsystem.services.AppointmentServiceImp;
import java.util.List;
import java.util.Optional;

@RequestMapping("/service-provider")
@RestController
@AllArgsConstructor
public class ServiceProviderController {

    private final ServiceProviderServiceImp ServiceProviderServiceImp;
    private final AppointmentServiceImp AppointmentServiceImp;

    @GetMapping("/get-service-provider")
    public ResponseEntity<List<ServiceProviderDto>> getDetailsOfProfession() {
        return ResponseEntity.ok(ServiceProviderServiceImp.getProfessional());
    }

    @GetMapping("/service-provider-by-id/{id}")
    public ResponseEntity<List<ServiceProviderDto>> getDetailsOfProfessionalById(@PathVariable("id") String id) {
        return ResponseEntity.ok(ServiceProviderServiceImp.serviceProviderById(id));
    }

    @PostMapping("/add-service-provider")
    public ResponseEntity<ServiceProviderDto> addProfessionDetail(@RequestBody ServiceProviderDto serviceProviderDTO) {
        return ResponseEntity.of(Optional.of(ServiceProviderServiceImp.addServiceProvider(serviceProviderDTO)));
    }

    @DeleteMapping("/delete-service-provider/{id}")
    public ResponseEntity<String> deleteProfession(@PathVariable("id") String id) {
        ServiceProviderServiceImp.deleteProfession(id);
        return ResponseEntity.ok("Service Provider deleted");
    }

    @PatchMapping("/update-service-provider/{email}")
    public ResponseEntity<ServiceProviderDto> updateProfession(
            @PathVariable("email") String email,
            @RequestBody ServiceProviderDto serviceProviderDTO
    ) {
        return ResponseEntity.ok()
                .body(ServiceProviderServiceImp.updateServiceProviderDetails(email, serviceProviderDTO));
    }



    @PostMapping("/add-daytime/{id}")
    public ResponseEntity<List<AvailableWeekDayDto>> addWeekdays(
            @PathVariable("id") String id,
            @RequestBody List<AvailableWeekDayDto> availableWeekDayDTOS
    ) {
        return ResponseEntity.ok(ServiceProviderServiceImp.addDayAndTime(id, availableWeekDayDTOS));
    }

    @PostMapping("/single-daytime/{id}")
    public ResponseEntity<AvailableWeekDayDto> addSingleTime(
            @PathVariable("id") String id,
            @RequestBody AvailableWeekDayDto availableWeekDay
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                ServiceProviderServiceImp.addSingleAvailableWeekDay(id, availableWeekDay)
        );
    }

    @DeleteMapping("/delete-day/{id}/{day}")
    public ResponseEntity<String> deleteDayAndTime(@PathVariable("id") String id, @PathVariable("day") String day) {
            ServiceProviderServiceImp.deleteDayAndTime(id, day);
            return ResponseEntity.ok().build();
    }

    @PatchMapping("/day-to-time-from-time/{id}")
    public ResponseEntity<AvailableWeekDayDto> updateDayAndTime(
            @PathVariable("id") String id,
            @RequestBody AvailableWeekDayDto availableWeekDayDTO
    ) {
        return ResponseEntity.ok().body(ServiceProviderServiceImp.updateDayAndTime(id, availableWeekDayDTO));
    }

    @GetMapping("/id-time/{id}")
    public ResponseEntity<List<AvailableWeekDayDto>> getAllDayAndTime(@PathVariable("id") String id) {
        List<AvailableWeekDayDto> list = ServiceProviderServiceImp.getAvailableWeekDayDetail(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/add-id-off-day/{id}")
    public ResponseEntity<List<OffDayDto>> addOfDay(
            @PathVariable("id") String id,
            @RequestBody List<OffDayDto> offDayDto
    ) {
        return ResponseEntity.ok(ServiceProviderServiceImp.addOffDay(id, offDayDto));
    }

    @PostMapping("/add-single-off-day/{id}")
    public ResponseEntity<OffDayDto> addSingleDay(@PathVariable("id") String id, @RequestBody OffDayDto offDayDTO) {
        return ResponseEntity.ok(ServiceProviderServiceImp.addOffDay(id, offDayDTO));
    }

    @PatchMapping("/update-single-off-day/{id}")
    public ResponseEntity<OffDayDto> updateOffDay(@PathVariable("id") String id, @RequestBody OffDayDto offDay) {
        OffDayDto offDayDTO = ServiceProviderServiceImp.updateOffDay(id, offDay);
        return ResponseEntity.ok().body(offDayDTO);
    }

    @PatchMapping("/update-list-off-day/{id}")
    public ResponseEntity<List<OffDayDto>> updateOfDayList(
            @PathVariable("id") String id,
            @RequestBody List<OffDayDto> offDays
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ServiceProviderServiceImp.updateOffDay(id, offDays));
    }

    @DeleteMapping("/delete-single-off-day/{id}/{type}")
    public ResponseEntity<String> deleteOffDay(@PathVariable("id") String id, @PathVariable("type") HolidayType type) {
            ServiceProviderServiceImp.deleteOffDay(id, type);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/get-off-day/{id}")
    public ResponseEntity<List<OffDayDto>> getAllHolidayDetails(@PathVariable("id") String id) {
        return ResponseEntity.ok(ServiceProviderServiceImp.getOffDayDetails(id));
    }

    @PostMapping("/add-location/{id}")
    public ResponseEntity<LocationDto> addLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderServiceImp.addProfessionalLocation(id, locationDTO));
    }

    @PatchMapping("/update-location/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") String id,
            @RequestBody LocationDto locationDTO
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderServiceImp.updateProfessionLocation(id, locationDTO));
    }

    @DeleteMapping("/delete-location/{id}/{type}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") String id, @PathVariable("type") String type) {
            ServiceProviderServiceImp.deleteLocation(id, type);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("service Provider location deleted");
    }

    @PostMapping("/add-break-time/{id}/{day}")
    public ResponseEntity<List<BreakTimeDto>> addBreakTime(
            @PathVariable("id") String id,
            @PathVariable("day") String day,
            @RequestBody List<BreakTimeDto> breakTimeDTOS
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderServiceImp.addBreakTime(id, day, breakTimeDTOS));
    }
    @PatchMapping("/update-break-time/{id}/{day}")
    public ResponseEntity<List<BreakTimeDto>> updateBreakTime(
            @PathVariable("id") String id,
            @PathVariable("day") String day,
            @RequestBody List<BreakTimeDto> breakTimeDTOS
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ServiceProviderServiceImp.updateBreakTime(id, day, breakTimeDTOS));
    }

    @DeleteMapping("/delete-break-time/{id}/{title}/{day}")
    public void deleteBreakTime1(
            @PathVariable("id") String id,
            @PathVariable("title") String title,
            @PathVariable("day") String day
    ) {
        ServiceProviderServiceImp.deleteBreakTimes(id, title, day);
    }

        @PatchMapping("/update-appointment-status/{id}")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable("id") String id,
            @RequestBody AppointmentDto appointment
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(AppointmentServiceImp.appointmentStatus(id, appointment));
    }

    @GetMapping("/all-appointment-detail-by-id/{id}")
    public ResponseEntity<List<AppointmentDto>> getAllAppointment(@PathVariable("id") String id) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(AppointmentServiceImp.getAppointments(id));
    }

}