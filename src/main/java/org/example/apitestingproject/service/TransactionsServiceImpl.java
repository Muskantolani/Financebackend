package org.example.apitestingproject.service;


import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.repository.TransactionRepository;
import org.example.apitestingproject.service.exceptions.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionsServiceImpl implements TransactionsService
{
    @Autowired
    TransactionRepository transactionsRepo;
    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionsRepo.save(transaction);
    }
    @Override
    public Transaction updateTransaction(int id, Transaction transaction) {
        Transaction existing = transactionsRepo.findById(id)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transaction not found with id " + id));

        existing.setPurchase(transaction.getPurchase());
        existing.setTransactionDate(transaction.getTransactionDate());
        existing.setAmountPaid(transaction.getAmountPaid());
        return transactionsRepo.save(existing);
    }

    @Override
    public void deleteTransaction(int id) {
        transactionsRepo.deleteById(id);
    }

    @Override
    public Iterable<Transaction> getAllTransactions() {
        return  transactionsRepo.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(int id) {
        return transactionsRepo.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionsRepo.findByPurchase_User_Id(userId);
    }
}







