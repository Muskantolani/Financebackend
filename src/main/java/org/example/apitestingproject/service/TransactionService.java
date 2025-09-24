package org.example.apitestingproject.service;




import org.example.apitestingproject.entities.Purchase;
import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.repository.PurchaseRepository;
import org.example.apitestingproject.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final PurchaseRepository purchaseRepository;

    public TransactionService(TransactionRepository repository, PurchaseRepository purchaseRepository) {
        this.repository = repository;
        this.purchaseRepository = purchaseRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    public Transaction updateTransaction(Integer id, Transaction transaction) {
        return repository.findById(id).map(existing -> {
            existing.setAmountPaid(transaction.getAmountPaid());
            existing.setTransactionType(transaction.getTransactionType());
            existing.setPurchase(transaction.getPurchase());
            existing.setAmountPaid(transaction.getAmountPaid());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Integer id) {
        repository.deleteById(id);
    }

    public Transaction getTransactionById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Iterable<Transaction> getAllTransactions() {
        return repository.findAll();
    }




    public Iterable<Transaction> getTransactionByUserId(Integer userId){


        List<Integer> purchaseIds = purchaseRepository.findByUser_Id(userId).stream().map(p-> p.getId()).collect(Collectors.toList());
        if (purchaseIds.isEmpty()) {
            return List.of(); // no purchases → no transactions
        }

        // 2. Get all transactions for those purchases
        return repository.findByPurchaseIdIn(purchaseIds);


    }
}
