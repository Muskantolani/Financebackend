package org.example.apitestingproject.controller;

import org.example.apitestingproject.dto.InstallmentSchedute;
import org.example.apitestingproject.service.InstallmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class InstallmentController {

    private final InstallmentService installmentService;

    public InstallmentController(InstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @GetMapping("/{userId}/installments")
    public ResponseEntity<List<InstallmentSchedute>> getUserInstallments(@PathVariable int userId) {
        List<InstallmentSchedute> schedules = installmentService.getSchedulesByUser(userId);
        return ResponseEntity.ok(schedules);
    }
}
