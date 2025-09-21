package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.InstallmentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
}
