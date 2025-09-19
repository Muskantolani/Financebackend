package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.InstallmentSchedule;
import org.springframework.data.repository.CrudRepository;

public interface InstallmentScheduleRepository extends CrudRepository<InstallmentSchedule,Integer> {
}
