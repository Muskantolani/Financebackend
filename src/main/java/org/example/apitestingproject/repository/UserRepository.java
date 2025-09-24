package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.OtpVerification;
import org.example.apitestingproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.*;

public interface UserRepository extends JpaRepository<User,Integer>
{
    Optional<User> findByEmail(String email);
}
