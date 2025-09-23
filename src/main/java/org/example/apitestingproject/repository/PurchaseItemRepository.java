package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.PurchaseItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseItemRepository extends CrudRepository<PurchaseItem,Integer> {
    List<PurchaseItem> findByPurchase_Id(Integer purchaseId);
}
