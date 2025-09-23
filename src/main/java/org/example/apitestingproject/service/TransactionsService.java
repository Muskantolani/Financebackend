package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionsService
{
    Transaction createTransaction(Transaction transaction);
    Transaction updateTransaction(int id, Transaction transaction);
    void deleteTransaction(int id);
    Iterable<Transaction> getAllTransactions();
    Optional<Transaction> getTransactionById(int id);
    List<Transaction> getTransactionsByUserId(int userId);
}
