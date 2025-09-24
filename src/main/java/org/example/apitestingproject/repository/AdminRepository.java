package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    List<Admin> findByRole(Admin.Role role);

    // For login check (username + password)
    Optional<Admin> findByUsernameAndPassword(String username, String password);
}