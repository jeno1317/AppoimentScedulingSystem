package org.appointmentschedulingsystem.repositories;

import org.appointmentschedulingsystem.entity.ServiceProvider;
import org.appointmentschedulingsystem.util.enums.ProfessionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ServiceProviderRepository extends MongoRepository<ServiceProvider, String> {

    default ServiceProvider findByIdOrElseThrow(String id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    List<ServiceProvider> findByProfessionName(ProfessionType professionName);

    ServiceProvider findByEmail(String email);

    @Query("{'location': { $near: { $geometry: { type: 'Point', coordinates:  ?0  }, $maxDistance: ?1 } } }")
    List<ServiceProvider> findByLocationNear(List<Double> coordinates, int maxDistance);

}

