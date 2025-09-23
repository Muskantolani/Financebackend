package org.example.apitestingproject.service;

import org.example.apitestingproject.DTO.InstallmentScheduleDTO;
import org.example.apitestingproject.DTO.InstallmentSchedute;
import org.example.apitestingproject.repository.InstallmentScheduleRepository;
import org.example.apitestingproject.repository.PurchaseItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstallmentService {

    private final InstallmentScheduleRepository scheduleRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    public InstallmentService(InstallmentScheduleRepository scheduleRepository, PurchaseItemRepository purchaseItemRepository) {
        this.scheduleRepository = scheduleRepository;

        this.purchaseItemRepository = purchaseItemRepository;
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
}
