package org.appointmentschedulingsystem.repositories;

import org.appointmentschedulingsystem.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    default Appointment findBookAppointmentsByBookIdOrElseThrow(String id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Appointment id not found"));
    }

}
