package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.PurchaseItem;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseItemRepository extends CrudRepository<PurchaseItem,Integer> {
}
