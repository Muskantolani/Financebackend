package org.example.apitestingproject.controller;

import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final PaymentService paymentService;

    public UserController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{userId}/pay-joining-fee")
    public ResponseEntity<Transaction> payJoiningFee(
            @PathVariable int userId,
            @RequestParam BigDecimal amount
    ) {
        Transaction txn = paymentService.payJoiningFee(userId, amount);
        return ResponseEntity.ok(txn);
    }
}

