package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.Bank;
import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.TransactionRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PaymentService {
    UserRepository userRepository;
    TransactionRepository transactionRepository;

    public PaymentService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction payJoiningFee(int userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getJoiningFeePaid() == User.YesNo.Yes) {
            throw new IllegalStateException("Joining fee already paid");
        }



        // Create transaction
        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setAmountPaid(amount);
        txn.setTransactionType(Transaction.TransactionType.JOINING_FEE);
        txn.setTransactionDate(LocalDate.now());
        transactionRepository.save(txn);

        // Update user status
        user.setJoiningFeePaid(User.YesNo.Yes);
        userRepository.save(user);

        return txn;
    }

}
