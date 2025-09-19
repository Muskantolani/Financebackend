package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.ApprovalStatus;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalStatusRepository extends CrudRepository<ApprovalStatus,Integer> {
}
