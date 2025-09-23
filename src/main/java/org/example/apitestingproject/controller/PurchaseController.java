package org.example.apitestingproject.controller;

import org.example.apitestingproject.DTO.EmiOptionResponse;
import org.example.apitestingproject.DTO.PurchaseRequest;
import org.example.apitestingproject.entities.*;
import org.example.apitestingproject.repository.EmiCardRepository;
import org.example.apitestingproject.service.EmiPreviewService;
import org.example.apitestingproject.service.ProductService;
import org.example.apitestingproject.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emi")
public class PurchaseController {

    private final ProductService productService;
    private final EmiPreviewService emiPreviewService;
    private  final EmiCardRepository emiCardRepository;
    private final PurchaseService purchaseService;


    public PurchaseController(ProductService productService, EmiPreviewService emiPreviewService, EmiCardRepository emiCardRepository, PurchaseService purchaseService) {
        this.productService = productService;
        this.emiPreviewService = emiPreviewService;
        this.emiCardRepository = emiCardRepository;
        this.purchaseService = purchaseService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<EmiOptionResponse>> getEMIPreview(
            @PathVariable int id,
            @RequestParam int cardId
    ) {
        Optional<Product> productOpt = productService.get(id);

        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = productOpt.get();
        BigDecimal productPrice = product.getCost();


        EmiCard card = fetchEmiCardById(cardId);
        List<EmiOptionResponse> emiOptions = emiPreviewService.generateEmiOptions(productPrice, card);

        return ResponseEntity.ok(emiOptions);
    }


    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequest request) {
        Purchase purchase = purchaseService.createPurchase(request);
        return ResponseEntity.ok(purchase);
    }


    // Pay a single installment
    @PostMapping("/installments/{installmentId}/pay")
    public ResponseEntity<Transaction> payInstallment(
            @PathVariable int installmentId,
            @RequestParam int userId) {
        Transaction txn = purchaseService.payInstallment(installmentId, userId);
        return ResponseEntity.ok(txn);
    }

    private EmiCard fetchEmiCardById(int cardId) {
        return emiCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("EmiCard not found with id: " + cardId));
    }



    @GetMapping("/overdue")
    public ResponseEntity<List<InstallmentSchedule>> getOverdueInstallments() {
        return ResponseEntity.ok(purchaseService.getOverdueInstallments());
    }



    // CRONJOB OCCURS BY ITSELF @ 00.00
    //Below func for manual trigger

    @PostMapping("/update-overdue")
    public ResponseEntity<String> updateOverdue() {
        int updated = purchaseService.updateOverdueInstallments();
        return ResponseEntity.ok(updated + " installments marked overdue");
    }

    @GetMapping("/testing")

    public ResponseEntity<String> findPendingBefore() {
        List<InstallmentSchedule> updated = purchaseService.findPendingFirst();
        return ResponseEntity.ok(updated + " installments marked overdue");
    }



    @PostMapping("/calculatePenalty")
    public ResponseEntity<String> calculatePenaltiesNow() {
        purchaseService.calculatePenalties();
        return ResponseEntity.ok("Penalties calculated successfully!");
    }



}
