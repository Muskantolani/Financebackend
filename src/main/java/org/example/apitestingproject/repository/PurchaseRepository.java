package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase,Integer> {
    List<Purchase> findByUser_Id(int userId);
}
