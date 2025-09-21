package org.example.apitestingproject.service;

import org.example.apitestingproject.DTO.*;
import org.example.apitestingproject.entities.*;
import org.example.apitestingproject.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmiCardRepository emiCardRepository;
    private final InstallmentScheduleRepository installmentScheduleRepository;


    public PurchaseService(PurchaseRepository purchaseRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           EmiCardRepository emiCardRepository, InstallmentScheduleRepository installmentScheduleRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.emiCardRepository = emiCardRepository;
        this.installmentScheduleRepository = installmentScheduleRepository;
    }

    public Purchase createPurchase(PurchaseRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        EmiCard card = emiCardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));


        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCard(card);
        purchase.setTenurePeriod(request.getTenurePeriod());
        purchase.setProcessingFeeApplied(request.getProcessingFee());
        purchase.setPurchaseDate(LocalDate.now());

        // Add each item
        List<PurchaseItemRequest> itemRequests = request.getItems();
        for (PurchaseItemRequest ir : itemRequests) {
            Product product = productRepository.findById(ir.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getProductStock() < ir.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getProductName());
            }

            product.setProductStock(product.getProductStock() - ir.getQuantity());
            productRepository.save(product);


            purchase.addItem(product, ir.getQuantity(), ir.getUnitPrice());
        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // generate installment schedules
        generateInstallmentSchedule(savedPurchase);
        return savedPurchase;
    }


    private void generateInstallmentSchedule(Purchase purchase) {
        int tenure = purchase.getTenurePeriod();
        BigDecimal totalPayable = purchase.getAmount().add(purchase.getProcessingFeeApplied());
        BigDecimal monthlyInstallment = totalPayable.divide(BigDecimal.valueOf(tenure), 2, BigDecimal.ROUND_HALF_UP);

        List<InstallmentSchedule> schedules = new ArrayList<>();


        for (int i = 1; i <= tenure; i++) {
            InstallmentSchedule schedule = new InstallmentSchedule();
            schedule.setPurchase(purchase);
            schedule.setInstallmentNo(i);
            schedule.setDueDate(purchase.getPurchaseDate().plusMonths(i));
            schedule.setInstallmentAmount(monthlyInstallment);
            schedule.setPaymentStatus(InstallmentSchedule.PaymentStatus.Pending);

            schedules.add(schedule);
        }

        installmentScheduleRepository.saveAll(schedules);
    }

}


