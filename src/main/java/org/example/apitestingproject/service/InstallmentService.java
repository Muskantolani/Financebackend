package org.example.apitestingproject.service;

import org.example.apitestingproject.dto.InstallmentSchedute;
import org.example.apitestingproject.repository.InstallmentScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstallmentService {

    private final InstallmentScheduleRepository scheduleRepository;

    public InstallmentService(InstallmentScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<org.example.apitestingproject.dto.InstallmentSchedute> getSchedulesByUser(int userId) {
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
}
