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
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository repository, PurchaseRepository purchaseRepository, NotificationService notificationService) {
        this.repository = repository;
        this.purchaseRepository = purchaseRepository;
        this.notificationService = notificationService;
    }

    public Transaction createTransaction(Transaction transaction) {
        Transaction saved = repository.save(transaction);

        // Send notification to the user who made the purchase
        Integer userId = saved.getPurchase().getUser().getId();
        notificationService.createNotification(userId,
                "Your payment of ₹" + saved.getAmountPaid() + " for purchase #" + saved.getPurchase().getId() + " was successful.");

        return saved;
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
