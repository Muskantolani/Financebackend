package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.PurchaseItem;
import org.example.apitestingproject.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {

    List<Transaction> findByPurchase_Id(Integer id);
    List<Transaction> findByPurchaseIdIn(List<Integer> purchaseIds);
    List<Transaction> findByPurchase_User_Id(int userId);
    Transaction findFirstByInstallmentIdOrderByTransactionDateAsc(int installmentId);


}
