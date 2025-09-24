package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.InstallmentSchedule;
import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.InstallmentScheduleRepository;
import org.example.apitestingproject.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRiskService {

    private final InstallmentScheduleRepository installmentRepository;
    private final TransactionRepository transactionRepository;

    public UserRiskService(InstallmentScheduleRepository installmentRepository,
                           TransactionRepository transactionRepository) {
        this.installmentRepository = installmentRepository;
        this.transactionRepository = transactionRepository;
    }

    public User.UserRiskProfile evaluateUser(int userId) {
        // Fetch all installments for this user
        List<InstallmentSchedule> allInstallments = installmentRepository.findByPurchaseUserId(userId);

        if (allInstallments.isEmpty()) {
            // No installments yet, treat as average
            return User.UserRiskProfile.AVERAGE;
        }

        long total = allInstallments.size();

        // Count overdue installments
        long overdueCount = allInstallments.stream()
                .filter(inst -> inst.getPaymentStatus() == InstallmentSchedule.PaymentStatus.Overdue)
                .count();

        // Count paid installments
        long paidCount = allInstallments.stream()
                .filter(inst -> inst.getPaymentStatus() == InstallmentSchedule.PaymentStatus.Paid)
                .count();

        double overduePercent = (overdueCount * 100.0) / total;
        double paidPercent = (paidCount * 100.0) / total;

        if (overduePercent >= 90.0) {
            return User.UserRiskProfile.BAD;
        } else if (paidPercent >= 90.0) {
            return User.UserRiskProfile.GOOD;
        } else {
            return User.UserRiskProfile.AVERAGE;
        }
    }



}
