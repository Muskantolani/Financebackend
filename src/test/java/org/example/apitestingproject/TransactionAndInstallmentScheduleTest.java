package org.example.apitestingproject;

import org.example.apitestingproject.entities.*;
import org.example.apitestingproject.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class TransactionAndInstallmentScheduleTest {

    @Autowired private PurchaseRepository purchaseRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private InstallmentScheduleRepository scheduleRepository;

    @Test
    void populateTransactionsAndSchedules() {

        // --- Fetch purchases ---
        Purchase purchase1 = purchaseRepository.findById(1).orElseThrow(); // user 4
        Purchase purchase2 = purchaseRepository.findById(2).orElseThrow(); // user 5
        Purchase purchase3 = purchaseRepository.findById(3).orElseThrow(); // user 6

        // --- Helper function to create schedule and transaction ---
        createInstallmentsAndTransaction(purchase1);
        createInstallmentsAndTransaction(purchase2);
        createInstallmentsAndTransaction(purchase3);
    }

    private void createInstallmentsAndTransaction(Purchase purchase) {
        int tenure = purchase.getTenurePeriod() != null ? purchase.getTenurePeriod() : 1;
        BigDecimal installmentAmount = purchase.getAmount()
                .add(purchase.getProcessingFeeApplied())
                .divide(BigDecimal.valueOf(tenure), 2, BigDecimal.ROUND_HALF_UP);

        LocalDate startDate = purchase.getPurchaseDate();

        Transaction firstTxn = null;

        for (int i = 1; i <= tenure; i++) {
            InstallmentSchedule schedule = new InstallmentSchedule();
            schedule.setPurchase(purchase);
            schedule.setInstallmentNo(i);
            schedule.setDueDate(startDate.plusMonths(i - 1));
            schedule.setInstallmentAmount(installmentAmount);

            // For the first installment, mark as paid and create transaction
            if (i == 1) {
                Transaction txn = new Transaction();
                txn.setPurchase(purchase);
                txn.setTransactionDate(startDate);
                txn.setAmountPaid(installmentAmount);
                firstTxn = transactionRepository.save(txn);

                schedule.setPaymentStatus(InstallmentSchedule.PaymentStatus.Paid);
                schedule.setPaidTransaction(firstTxn);
            } else {
                schedule.setPaymentStatus(InstallmentSchedule.PaymentStatus.Pending);
            }

            scheduleRepository.save(schedule);
        }
    }
}