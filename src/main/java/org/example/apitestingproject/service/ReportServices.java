package org.example.apitestingproject.service;

import org.springframework.stereotype.Service;

@Service
public interface ReportServices
{
    byte[] generatePaymentsCsv(int userId);
    byte[] generatePaymentsPdf(int userId);

    byte[] generatePurchasesCsv(int userId);
    byte[] generatePurchasesPdf(int userId);
}
