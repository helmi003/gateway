package com.esprit.microservice.gatwayfinal.repository;

import com.esprit.microservice.gatwayfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.role != 'admin' AND u.approve = false")
    List<User> findUsersExcludingAdminAndApproved();
}
