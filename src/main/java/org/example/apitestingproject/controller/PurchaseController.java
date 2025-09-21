package org.example.apitestingproject.controller;

import org.example.apitestingproject.DTO.EmiOptionResponse;
import org.example.apitestingproject.DTO.PurchaseRequest;
import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.entities.Product;
import org.example.apitestingproject.entities.Purchase;
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


    private EmiCard fetchEmiCardById(int cardId) {
        return emiCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("EmiCard not found with id: " + cardId));
    }



}
