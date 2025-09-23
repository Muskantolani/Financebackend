package org.example.apitestingproject.service;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.opencsv.CSVWriter;
import org.example.apitestingproject.entities.Purchase;
import org.example.apitestingproject.entities.Transaction;
import org.example.apitestingproject.repository.PurchaseRepository;
import org.example.apitestingproject.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ReportServicesImpl implements ReportServices
{
    @Autowired
    TransactionRepository transactionsRepo;
    @Autowired
    PurchaseRepository purchasesRepo;
    public byte[] generatePaymentsCsv(int userId) {
        List<Transaction> txns = transactionsRepo.findByPurchase_User_Id(userId);
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        csvWriter.writeNext(new String[]{"Transaction ID", "Purchase ID", "Date", "Amount Paid"});
        for (Transaction txn : txns) {
            csvWriter.writeNext(new String[]{
                    String.valueOf(txn.getId()),
                    String.valueOf(txn.getPurchase().getId()),
                    txn.getTransactionDate() != null ? txn.getTransactionDate().toString() : "",
                    String.valueOf(txn.getAmountPaid())
            });
        }
        try {
            csvWriter.close();
        } catch (Exception ignored) {}

        return stringWriter.toString().getBytes();
    }
    public byte[] generatePurchasesCsv(int userId) {
        List<Purchase> purchases = purchasesRepo.findByUser_Id(userId);

        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw);
        writer.writeNext(new String[]{
                "Purchase ID",
                "Product Name",
                "Card Number",
                "Purchase Date",
                "Amount",
                "Tenure Period",
                "Total Amount"
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Purchase p : purchases) {
            writer.writeNext(new String[]{
                    String.valueOf(p.getId()),
                    //p.getProduct() != null ? p.getProduct().getProductName() : "",
                    p.getCard() != null ? p.getCard().getCardNumber() : "",
                    p.getPurchaseDate() != null ? dateFormat.format(p.getPurchaseDate()) : "",
                    p.getAmount() != null ? String.valueOf(p.getAmount()) : "",
                    p.getTenurePeriod() != null ? String.valueOf(p.getTenurePeriod()) : "",
                    //p.getTotalAmount() != null ? String.valueOf(p.getTotalAmount()) : ""
            });
        }

        try { writer.close(); } catch (Exception ignored) {}
        return sw.toString().getBytes();
    }
    @Override
    public byte[] generatePaymentsPdf(int userId) {
        List<Transaction> txns = transactionsRepo.findByPurchase_User_Id(userId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Payment History").setBold().setFontSize(14).setMarginBottom(15));

        Table table = new Table(4).useAllAvailableWidth();
        table.addHeaderCell("Transaction ID");
        table.addHeaderCell("Purchase ID");
        table.addHeaderCell("Date");
        table.addHeaderCell("Amount Paid");

        for (Transaction txn : txns) {
            table.addCell(String.valueOf(txn.getId()));
            table.addCell(String.valueOf(txn.getPurchase().getId()));
            table.addCell(txn.getTransactionDate() != null ? txn.getTransactionDate().toString() : "");
            table.addCell(String.valueOf(txn.getAmountPaid()));
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    @Override
    public byte[] generatePurchasesPdf(int userId) {
        List<Purchase> purchases = purchasesRepo.findByUser_Id(userId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Purchase History").setBold().setFontSize(14).setMarginBottom(15));

        Table table = new Table(7).useAllAvailableWidth();
        table.addHeaderCell(new Cell().add(new Paragraph("Purchase ID")));
        table.addHeaderCell("Product Name");
        table.addHeaderCell("Card Number");
        table.addHeaderCell("Purchase Date");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Tenure Period");
        table.addHeaderCell("Total Amount");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Purchase p : purchases) {
            table.addCell(String.valueOf(p.getId()));
            //table.addCell(p.getProduct() != null ? p.getProduct().getProductName() : "");
            table.addCell(p.getCard() != null ? p.getCard().getCardNumber() : "");
            table.addCell(p.getPurchaseDate() != null ? dateFormat.format(p.getPurchaseDate()) : "");
            table.addCell(p.getAmount() != null ? p.getAmount().toString() : "");
            table.addCell(p.getTenurePeriod() != null ? p.getTenurePeriod().toString() : "");
           // table.addCell(p.getTotalAmount() != null ? p.getTotalAmount().toString() : "");
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}
