package org.example.apitestingproject.service;

import org.example.apitestingproject.DTO.InstallmentScheduleDTO;
import org.example.apitestingproject.DTO.InstallmentSchedute;
import org.example.apitestingproject.entities.InstallmentSchedule;
import org.example.apitestingproject.repository.InstallmentScheduleRepository;
import org.example.apitestingproject.repository.PurchaseItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstallmentService {

    private final InstallmentScheduleRepository scheduleRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final NotificationService notificationService;

    public InstallmentService(InstallmentScheduleRepository scheduleRepository, PurchaseItemRepository purchaseItemRepository, NotificationService notificationService) {
        this.scheduleRepository = scheduleRepository;

        this.purchaseItemRepository = purchaseItemRepository;
        this.notificationService = notificationService;
    }

    public List<InstallmentSchedute> getSchedulesByUser(int userId) {


        return scheduleRepository.findAllByUserId(userId)
                .stream()
                .map(s -> new InstallmentSchedute(
                        s.getId(),
                        s.getDueDate(),
                        s.getInstallmentAmount(),
                        s.getInstallmentNo(),
                        s.getPaymentStatus().name(),
                        s.getPaidTransaction(),
                        s.getPurchase().getId()
                ))
                .toList();
    }



    public List<InstallmentScheduleDTO> getScheduleandProdcutsByUser(int userId) {
        return scheduleRepository.findAllByUserId(userId).
                stream().map(s -> {
                    List<String> productNames = purchaseItemRepository.findByPurchase_Id(s.getPurchase().getId()).stream().toList().
                            stream().map(item -> item.getProduct().getProductName()).toList();

                    return new InstallmentScheduleDTO(
                            s.getId(),
                            s.getDueDate(),
                            s.getInstallmentAmount(),
                            s.getInstallmentNo(),
                            s.getPaymentStatus().name(),
                            s.getPaidTransaction(),
                            s.getPurchase().getId(),
                            productNames,
                            s.getPenaltyAmount()
                    );

                })
                .toList();

    }

    // Scheduled to run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void notifyOverdueInstallments() {
        List<InstallmentSchedule> overdueInstallments = scheduleRepository.findByPaymentStatus(InstallmentSchedule.PaymentStatus.Overdue);

        for (InstallmentSchedule installment : overdueInstallments) {
            Integer userId = installment.getPurchase().getUser().getId();
            notificationService.createNotification(userId,
                    "Your installment #" + installment.getInstallmentNo() +
                            " of ₹" + installment.getInstallmentAmount() + " is overdue.");
        }
    }
}
