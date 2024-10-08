package org.appointmentschedulingsystem.mapper;

import org.appointmentschedulingsystem.config.MapStructConfig;
import org.appointmentschedulingsystem.dtos.AppointmentDto;
import org.appointmentschedulingsystem.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface AppointmentMapper {

    AppointmentMapper INSTANCE= Mappers.getMapper(AppointmentMapper.class);

    Appointment appointmentToAppointment(AppointmentDto appointmentDTO);
    AppointmentDto appointmentToAppointmentDTO(Appointment appointment);
    List<AppointmentDto> appointmentsToAppointmentDTO(List<Appointment> appointments);

}
