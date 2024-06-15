package org.appointmentschedulingsystem.services;

import org.appointmentschedulingsystem.dtos.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    AppointmentDto bookAppointment(String uid, String sid, AppointmentDto appointmentDTO);
    void removeAppointment(String id);
    AppointmentDto updateAppointment(String id, AppointmentDto appointmentDTO);
    AppointmentDto appointmentStatus(String id, AppointmentDto appointmentDTO);
    List<AppointmentDto> getAppointments(String id);
}
