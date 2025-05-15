package com.example.etb.repository;

import com.example.etb.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    // Custom query method to find users by their role
    List<UserModel> findByRole_Name(String role);
}
