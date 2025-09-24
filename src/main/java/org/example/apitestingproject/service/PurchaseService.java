package org.example.apitestingproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.apitestingproject.DTO.*;
import org.example.apitestingproject.entities.*;
import org.example.apitestingproject.repository.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmiCardRepository emiCardRepository;
    private final InstallmentScheduleRepository installmentScheduleRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private static final BigDecimal PENALTY_RATE = new BigDecimal("0.02"); // 2% per month



    public PurchaseService(PurchaseRepository purchaseRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           EmiCardRepository emiCardRepository, InstallmentScheduleRepository installmentScheduleRepository, TransactionRepository transactionRepository, AuditLogRepository auditLogRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.emiCardRepository = emiCardRepository;
        this.installmentScheduleRepository = installmentScheduleRepository;
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmiCard card = emiCardRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Card not found for user " + request.getUserId()));


        EmiCard.ActivationStatus cardActivationStatus = card.getActivationStatus();

        if (cardActivationStatus != EmiCard.ActivationStatus.Active) {
            throw new RuntimeException("Card is not active");
        }

        if (card.getValidTill().isBefore(LocalDate.now())) {
            throw new RuntimeException("Card is expired");
        }

        BigDecimal purchaseAmount = request.getItems().stream()
                .map(ir -> ir.getUnitPrice().multiply(BigDecimal.valueOf(ir.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        if (card.getAvailableLimit().compareTo(purchaseAmount) < 0) {
            throw new RuntimeException("Insufficient card limit");
        }


        card.setAvailableLimit(card.getAvailableLimit().subtract(purchaseAmount));
        emiCardRepository.save(card);


        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCard(card);
        purchase.setTenurePeriod(request.getTenurePeriod());
        purchase.setProcessingFeeApplied(request.getProcessingFee());
        purchase.setPurchaseDate(LocalDate.now());




        // Add each item
        List<PurchaseItemRequest> itemRequests = request.getItems();
        for (PurchaseItemRequest ir : itemRequests) {
            Product product = productRepository.findById(ir.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getProductStock() < ir.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getProductName());
            }

            product.setProductStock(product.getProductStock() - ir.getQuantity());
            productRepository.save(product);


            purchase.addItem(product, ir.getQuantity(), ir.getUnitPrice());
        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // generate installment schedules
        generateInstallmentSchedule(savedPurchase);


        return savedPurchase;
    }


    private void generateInstallmentSchedule(Purchase purchase)  {
        int tenure = purchase.getTenurePeriod();
        BigDecimal totalPayable = purchase.getAmount().add(purchase.getProcessingFeeApplied());
        BigDecimal monthlyInstallment = totalPayable.divide(BigDecimal.valueOf(tenure), 2, BigDecimal.ROUND_HALF_UP);

        List<InstallmentSchedule> schedules = new ArrayList<>();




        for (int i = 1; i <= tenure; i++) {
            InstallmentSchedule schedule = new InstallmentSchedule();
            schedule.setPurchase(purchase);
            schedule.setInstallmentNo(i);
            schedule.setDueDate(purchase.getPurchaseDate().plusMonths(i));
            schedule.setInstallmentAmount(monthlyInstallment);
            schedule.setPaymentStatus(InstallmentSchedule.PaymentStatus.Pending);

            schedules.add(schedule);
        }

        installmentScheduleRepository.saveAll(schedules);
    }


    public List<ProductDTO> getProductsByUser(int userId) {
        return purchaseRepository.findByUser_Id(userId)
                .stream()
                .flatMap(p -> p.getItems().stream())
                .map(PurchaseItem::getProduct)
                .distinct()  // remove duplicates if same product bought multiple times
                .map(p -> new ProductDTO(p.getId(), p.getProductName(), p.getCost(), p.getCategory()))
                .toList();
    }



    @Transactional
    public Transaction payInstallment(int installmentId, int userId, String paymentMethod) throws JsonProcessingException {
        InstallmentSchedule inst = installmentScheduleRepository.findById(installmentId).orElseThrow();
        if (inst.getPurchase().getUser().getId() != userId)
            throw new RuntimeException("Unauthorized");

        if (inst.getPaymentStatus() == InstallmentSchedule.PaymentStatus.Paid)
            throw new RuntimeException("Already paid");

        //if penalty amount then add penalty amount too
        BigDecimal amt = inst.getInstallmentAmount()
                .add(Optional.ofNullable(inst.getPenaltyAmount()).orElse(BigDecimal.ZERO));

        BigDecimal penaltyAmt = Optional.ofNullable(inst.getPenaltyAmount()).orElse(BigDecimal.ZERO);
        BigDecimal totalAmt = inst.getInstallmentAmount().add(penaltyAmt);

        Transaction txn = new Transaction();
        txn.setPurchase(inst.getPurchase());
        txn.setAmountPaid(amt);
        txn.setTransactionType(Transaction.TransactionType.INSTALLMENT);
        txn.setTransaction_method(paymentMethod);
        txn.setTransactionDate(LocalDate.now());
        transactionRepository.save(txn);

        inst.setPaymentStatus(InstallmentSchedule.PaymentStatus.Paid);
        inst.setPaidTransaction(txn);
        installmentScheduleRepository.save(inst);

        String serverIp = "UNKNOWN";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Audit

        Map<String, Object> actionDetails = new HashMap<>();
        actionDetails.put("installmentId", inst.getId());
        actionDetails.put("purchaseId", inst.getPurchase().getId());
        actionDetails.put("userId", userId);
        actionDetails.put("amount", totalAmt);
        if (penaltyAmt != BigDecimal.ZERO){
        actionDetails.put("penaltyAmount", penaltyAmt);
        }
        actionDetails.put("transactionType", txn.getTransactionType().name());
        actionDetails.put("transactionMethod", txn.getTransaction_method());
        actionDetails.put("paymentStatus", "SUCCESS");
        actionDetails.put("timestamp", LocalDateTime.now().toString());

        AuditLog audit = new AuditLog();
        audit.setUser(inst.getPurchase().getUser());
        audit.setActionType(AuditLog.ActionType.Payment);
        audit.setActionTimestamp(LocalDateTime.now());
        audit.setIpAddress(serverIp);
        audit.setActionDetails(new ObjectMapper().writeValueAsString(actionDetails));

        auditLogRepository.save(audit);

        return txn;
    }




    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight
    public int updateOverdueInstallments() throws JsonProcessingException {

        List<Integer> allInstIds = installmentScheduleRepository.findOverdueInstallmentIds(InstallmentSchedule.PaymentStatus.Pending,LocalDate.now());
        int updated = installmentScheduleRepository.markOverdueInstallments(InstallmentSchedule.PaymentStatus.Overdue, InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
        System.out.println("Updated " + updated + " overdue installments");

        String serverIp = "UNKNOWN";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Audit

        for (Integer instId : allInstIds) {
            String finalServerIp = serverIp;
            installmentScheduleRepository.findById(instId).ifPresent(installmentSchedule -> {
                try {
                    Map<String, Object> actionDetails = new HashMap<>();
                    actionDetails.put("installmentId", instId);
                    actionDetails.put("status", "Changed to Overdue");
                    actionDetails.put("timestamp", LocalDateTime.now().toString());


                    AuditLog audit = new AuditLog();
                    audit.setUser(installmentSchedule.getPurchase().getUser());
                    audit.setActionType(AuditLog.ActionType.InstallmentStatusUpdate);
                    audit.setActionTimestamp(LocalDateTime.now());
                    audit.setIpAddress(finalServerIp);
                    audit.setActionDetails(new ObjectMapper().writeValueAsString(actionDetails));

                    auditLogRepository.save(audit);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }


        return updated;
    }

    public List<InstallmentSchedule> getOverdueInstallments() {

        return installmentScheduleRepository.findByPaymentStatus(InstallmentSchedule.PaymentStatus.Overdue);
    }

    public List<InstallmentSchedule> findPendingFirst() {
        System.out.println("HIII");
        System.out.println(installmentScheduleRepository.findPendingBefore(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now()));

        return installmentScheduleRepository.findPendingBefore(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
    }


    @Transactional
    public void calculatePenalties() throws JsonProcessingException {
        List<InstallmentSchedule> overdueSchedules =
                installmentScheduleRepository.findOverdueSchedules(
                        InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
        System.out.println("Overdue schedules: " + overdueSchedules.size());

        String serverIp = "UNKNOWN";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }



        for (InstallmentSchedule schedule : overdueSchedules) {
            long daysOverdue = ChronoUnit.DAYS.between(schedule.getDueDate(), LocalDate.now());

            // Grace period
            int graceDays = 5;
            if (daysOverdue <= graceDays) {
                schedule.setPenaltyAmount(BigDecimal.ZERO);
                continue;
            }

            // Exponential penalty slab logic
            BigDecimal installmentAmount = schedule.getInstallmentAmount();
            BigDecimal penalty = calculateExponentialPenalty(daysOverdue - graceDays, installmentAmount);
            schedule.setPenaltyAmount(penalty);

            ObjectMapper mapper = new ObjectMapper();
            // Create new audit log
            Map<String, Object> actionDetails = new HashMap<>();
            actionDetails.put("installmentId", schedule.getId());
            actionDetails.put("purchaseId", schedule.getPurchase().getId());
            actionDetails.put("status", "Changed to Overdue");
            actionDetails.put("penaltyAmount", penalty);

            AuditLog audit = new AuditLog();
            audit.setUser(schedule.getPurchase().getUser());
            audit.setActionType(AuditLog.ActionType.InstallmentStatusUpdate);
            audit.setActionTimestamp(LocalDateTime.now());
            audit.setIpAddress(serverIp);
            audit.setActionDetails(mapper.writeValueAsString(actionDetails));


//
//
//            Map<String, Object> actionDetails = new HashMap<>();
//            actionDetails.put("installmentId", instId);
//            actionDetails.put("status", "Changed to Overdue");
//            actionDetails.put("timestamp", LocalDateTime.now().toString());

//
//            AuditLog audit = new AuditLog();
//            audit.setUser(installmentSchedule.getPurchase().getUser());
//            audit.setActionType(AuditLog.ActionType.InstallmentStatusUpdate);
//            audit.setActionTimestamp(LocalDateTime.now());
//            audit.setIpAddress(finalServerIp);
//            audit.setActionDetails(new ObjectMapper().writeValueAsString(actionDetails));

            auditLogRepository.save(audit);
        }

        installmentScheduleRepository.saveAll(overdueSchedules);
    }

//    private BigDecimal calculateExponentialPenalty(long days, BigDecimal principal) {
//        // Example slab-wise exponential growth:
//        // 1-10 days: 1.01^days
//        // 11-20 days: 1.02^days
//        // >20 days: 1.03^days
//        BigDecimal rate;
//
//        if (days <= 10) rate = new BigDecimal("0.005");
//        else if (days <= 20) rate = new BigDecimal("0.01");
//        else rate = new BigDecimal("0.02");
//
//        // Exponential formula: principal * ((1 + rate)^days - 1)
//        BigDecimal base = BigDecimal.ONE.add(rate);
//
//        // BigDecimal.pow takes int, and MathContext for precision
//        BigDecimal multiplier = base.pow((int) days, new MathContext(10, RoundingMode.HALF_UP));
//
//        return principal.multiply(multiplier.subtract(BigDecimal.ONE));
//    }
//
//


    private BigDecimal calculateExponentialPenalty(long days, BigDecimal principal) {
        BigDecimal rate = new BigDecimal("0.003"); // 0.5% per day
        BigDecimal mcMultiplier;

        if (days <= 15) {

            mcMultiplier = BigDecimal.ONE.add(rate).pow((int) days, new MathContext(10, RoundingMode.HALF_UP));
        } else {

            BigDecimal expPart = BigDecimal.ONE.add(rate).pow(15, new MathContext(10, RoundingMode.HALF_UP)).subtract(BigDecimal.ONE);
            BigDecimal linearPart = rate.multiply(BigDecimal.valueOf(days - 15));
            mcMultiplier = BigDecimal.ONE.add(expPart.add(linearPart));
        }

        return principal.multiply(mcMultiplier.subtract(BigDecimal.ONE), new MathContext(10, RoundingMode.HALF_UP));
    }


    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight run penalty recalculation
    public void runPenaltyCalculation() throws JsonProcessingException {
        calculatePenalties();
    }



//    @Transactional
//    public Transaction payInstallment(int installmentId, int userId) {
//        InstallmentSchedule inst = installmentScheduleRepository.findById(installmentId)
//                .orElseThrow(() -> new RuntimeException("Installment not found"));
//
//        // Validate user
//        if (inst.getPurchase().getUser().getId() != userId) {
//            throw new RuntimeException("Unauthorized");
//        }
//
//        if (inst.getPaymentStatus() == InstallmentSchedule.PaymentStatus.Paid) {
//            throw new RuntimeException("Already paid");
//        }
//
//        User user = inst.getPurchase().getUser();
//        Bank bank = user.getBank();
//
//        // Calculate penalty if overdue
//        int monthsOverdue = Period.between(inst.getDueDate(), LocalDate.now()).getMonths();
//        BigDecimal penalty = BigDecimal.ZERO;
//        if (monthsOverdue > 0) {
//            BigDecimal PENALTY_RATE = new BigDecimal("0.02"); // 2% per month
//            penalty = inst.getInstallmentAmount()
//                    .multiply(PENALTY_RATE)
//                    .multiply(BigDecimal.valueOf(monthsOverdue))
//                    .setScale(2, BigDecimal.ROUND_HALF_UP);
//            inst.setPenaltyAmount(penalty);
//        }
//
//        BigDecimal totalDue = inst.getInstallmentAmount().add(inst.getPenaltyAmount());
//
//
//
//        // Create transaction
//        Transaction txn = new Transaction();
//        txn.setPurchase(inst.getPurchase());
//        txn.setAmountPaid(totalDue);
//        txn.setTransactionType(Transaction.TransactionType.INSTALLMENT);
//        txn.setTransactionDate(LocalDate.now());
//        transactionRepository.save(txn);
//
//        // Update installment
//        inst.setPaymentStatus(InstallmentSchedule.PaymentStatus.Paid);
//        inst.setPaidTransaction(txn);
//        inst.setPenaltyAmount(BigDecimal.ZERO); // reset after payment
//        installmentScheduleRepository.save(inst);
//
//
//
//        return txn;
//    }
//
//
//
//


}





