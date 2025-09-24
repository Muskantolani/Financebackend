package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.InstallmentSchedule;
import org.example.apitestingproject.entities.Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InstallmentScheduleRepository extends JpaRepository<InstallmentSchedule,Integer> {



    @Query("""
            SELECT s 
            FROM InstallmentSchedule s
            JOIN s.purchase p
            WHERE p.user.id = :userId
            ORDER BY s.dueDate ASC
            """)
    List<InstallmentSchedule> findAllByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE InstallmentSchedule i " +
            "SET i.paymentStatus = :overdue " +
            "WHERE i.dueDate < :today " +
            "AND i.paymentStatus = :pending")
    int markOverdueInstallments(@Param("overdue") InstallmentSchedule.PaymentStatus overdue,
                                @Param("pending") InstallmentSchedule.PaymentStatus pending,
                                @Param("today") LocalDate today);



    @Query("SELECT i.id FROM InstallmentSchedule i " +
            "WHERE i.dueDate < :today " +
            "AND i.paymentStatus = :pending")
    List<Integer> findOverdueInstallmentIds(@Param("pending") InstallmentSchedule.PaymentStatus pending,
                                       @Param("today") LocalDate today);

    List<InstallmentSchedule> findByPaymentStatus(InstallmentSchedule.PaymentStatus paymentStatus);


    @Query("SELECT i FROM InstallmentSchedule i " +
            "WHERE i.paymentStatus = :pending " +
            "AND i.dueDate < :today")
    List<InstallmentSchedule> findOverdueSchedules(
            @Param("pending") InstallmentSchedule.PaymentStatus pending,
            @Param("today") LocalDate today
    );


    @Query("SELECT i FROM InstallmentSchedule i " +
            "WHERE i.dueDate < :today AND i.paymentStatus = :pending")
    List<InstallmentSchedule> findPendingBefore(@Param("pending") InstallmentSchedule.PaymentStatus pending,
                                                @Param("today") LocalDate today);



// org.example.model.PaymentStatus.Overdue
}
