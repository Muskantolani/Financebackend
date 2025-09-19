package org.example.apitestingproject;


import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.entities.Purchase;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class PurchaseTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CardTypeRepository cardTypeRepository;
    @Autowired private EmiCardRepository emiCardRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PurchaseRepository purchaseRepository;

    @Test
    void populatePurchases() {

        // ============ USER 4 (CardType 1) ============
        User user4 = userRepository.findById(4).orElseThrow();
        EmiCard card4 = emiCardRepository.findByUser(user4).orElseThrow();

        Purchase purchase1 = new Purchase();
        purchase1.setUser(user4);
        purchase1.setCard(card4);
        purchase1.setPurchaseDate(LocalDate.now());
        purchase1.setTenurePeriod(6);
        purchase1.setProcessingFeeApplied(BigDecimal.valueOf(200));

        // Add two products
        Product prod1 = productRepository.findById(1).orElseThrow();
        Product prod2 = productRepository.findById(2).orElseThrow();
        purchase1.addItem(prod1, 2, prod1.getCost());  // qty 2 x 1500 = 3000
        purchase1.addItem(prod2, 1, prod2.getCost());  // qty 1 x 2000 = 2000

        purchaseRepository.save(purchase1);


        // ============ USER 5 (CardType 2) ============
        User user5 = userRepository.findById(5).orElseThrow();
        EmiCard card5 = emiCardRepository.findByUser(user5).orElseThrow();

        Purchase purchase2 = new Purchase();
        purchase2.setUser(user5);
        purchase2.setCard(card5);
        purchase2.setPurchaseDate(LocalDate.now());
        purchase2.setTenurePeriod(12);
        purchase2.setProcessingFeeApplied(BigDecimal.valueOf(500));

        Product prod3 = productRepository.findById(3).orElseThrow();
        Product prod4 = productRepository.findById(4).orElseThrow();
        purchase2.addItem(prod3, 1, prod3.getCost());  // 10000
        purchase2.addItem(prod4, 2, prod4.getCost());   // 5000

        purchaseRepository.save(purchase2);


        // ============ USER 6 (CardType 2) ============
        User user6 = userRepository.findById(6).orElseThrow();
        EmiCard card6 = emiCardRepository.findByUser(user6).orElseThrow();

        Purchase purchase3 = new Purchase();
        purchase3.setUser(user6);
        purchase3.setCard(card6);
        purchase3.setPurchaseDate(LocalDate.now());
        purchase3.setTenurePeriod(9);
        purchase3.setProcessingFeeApplied(BigDecimal.valueOf(300));

        Product prod5 = productRepository.findById(5).orElseThrow();
        Product prod6 = productRepository.findById(6).orElseThrow();
        Product prod7 = productRepository.findById(7).orElseThrow();
        purchase3.addItem(prod5, 1, BigDecimal.valueOf(7000));
        purchase3.addItem(prod6, 3, BigDecimal.valueOf(1200));   // 3600
        purchase3.addItem(prod7, 2, BigDecimal.valueOf(4000));   // 8000

        purchaseRepository.save(purchase3);
    }
}
