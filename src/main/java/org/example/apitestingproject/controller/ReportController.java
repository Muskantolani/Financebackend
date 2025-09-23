package org.example.apitestingproject.controller;



import org.example.apitestingproject.service.ReportServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    ReportServices reportService;
    @GetMapping("/payments/csv/{userId}")
    public ResponseEntity<byte[]> getPaymentsCsv(@PathVariable int userId) {
        byte[] data = reportService.generatePaymentsCsv(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payments.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    @GetMapping("/payments/pdf/{userId}")
    public ResponseEntity<byte[]> getPaymentsPdf(@PathVariable int userId) {
        byte[] data = reportService.generatePaymentsPdf(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payments.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
    @GetMapping("/purchases/csv/{userId}")
    public ResponseEntity<byte[]> getPurchasesCsv(@PathVariable int userId) {
        byte[] data = reportService.generatePurchasesCsv(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=purchases.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }
    @GetMapping("/purchases/pdf/{userId}")
    public ResponseEntity<byte[]> getPurchasesPdf(@PathVariable int userId) {
        byte[] data = reportService.generatePurchasesPdf(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=purchases.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}

