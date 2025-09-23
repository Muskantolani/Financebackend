package org.example.apitestingproject.controller;


import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Transaction>> getTransactionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getTransactionByUserId(userId));
    }

}
