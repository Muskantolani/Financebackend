package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Bank;
import org.springframework.data.repository.CrudRepository;

public interface BankRepository extends CrudRepository<Bank,Integer> {
}
