package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Purchase;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<Purchase,Integer> {
}
