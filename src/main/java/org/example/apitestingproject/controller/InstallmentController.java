package org.example.apitestingproject.controller;

import org.example.apitestingproject.DTO.InstallmentScheduleDTO;
import org.example.apitestingproject.DTO.InstallmentSchedute;
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


    @GetMapping("/{userId}/installments_w_products")
    public ResponseEntity<List<InstallmentScheduleDTO>> getScheduleandProdcutsByUser(@PathVariable int userId) {
        List<InstallmentScheduleDTO> schedules = installmentService.getScheduleandProdcutsByUser(userId);
        return ResponseEntity.ok(schedules);
    }



}
