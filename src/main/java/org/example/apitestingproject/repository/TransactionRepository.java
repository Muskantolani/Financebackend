package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
}
