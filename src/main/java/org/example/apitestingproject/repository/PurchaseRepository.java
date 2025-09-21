package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
    List<Purchase> findByUser_Id(int userId);

}
