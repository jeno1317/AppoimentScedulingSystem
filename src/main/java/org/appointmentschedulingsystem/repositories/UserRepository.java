package org.appointmentschedulingsystem.repositories;

import org.appointmentschedulingsystem.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    default User findByIdOrElseThrow(String id) {
        return findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByEmail(String email);

}

