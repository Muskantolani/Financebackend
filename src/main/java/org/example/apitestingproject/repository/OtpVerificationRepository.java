package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.OtpVerification;
import org.example.apitestingproject.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpVerificationRepository extends CrudRepository<OtpVerification,Integer> {
    Optional<OtpVerification> findTopByUserAndStatusOrderByOtpTimestampDesc(User user, OtpVerification.Status status);

    @Modifying
    @Query("UPDATE OtpVerification o SET o.status = :newStatus WHERE o.expiresAt < :currentTime AND o.status = org.example.apitestingproject.entities.OtpVerification$Status.Pending")
    int markExpiredOtps(@Param("currentTime") LocalDateTime currentTime,
                        @Param("newStatus") OtpVerification.Status newStatus);
}
