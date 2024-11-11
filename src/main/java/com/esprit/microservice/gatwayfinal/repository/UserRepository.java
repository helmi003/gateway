package com.esprit.microservice.gatwayfinal.repository;

import com.esprit.microservice.gatwayfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
