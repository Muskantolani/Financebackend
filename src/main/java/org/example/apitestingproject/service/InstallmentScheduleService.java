package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.InstallmentSchedule;
import org.example.apitestingproject.repository.InstallmentScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstallmentScheduleService {

    private final InstallmentScheduleRepository installmentRepository;

    public InstallmentScheduleService(InstallmentScheduleRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // runs every day at midnight
    @Transactional
    public void markOverdueInstallments() {
        List<InstallmentSchedule> pendingInstallments =
                installmentRepository.findByPaymentStatus(InstallmentSchedule.PaymentStatus.Pending);

        LocalDate today = LocalDate.now();
        for (InstallmentSchedule inst : pendingInstallments) {
            if (inst.getDueDate().isBefore(today)) {
                inst.setPaymentStatus(InstallmentSchedule.PaymentStatus.Overdue);
                installmentRepository.save(inst);
            }
        }
    }
}

