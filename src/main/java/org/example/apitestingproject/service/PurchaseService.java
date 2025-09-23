package org.example.apitestingproject.service;

import org.example.apitestingproject.DTO.*;
import org.example.apitestingproject.entities.*;
import org.example.apitestingproject.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmiCardRepository emiCardRepository;
    private final InstallmentScheduleRepository installmentScheduleRepository;
    private final TransactionRepository transactionRepository;
    private static final BigDecimal PENALTY_RATE = new BigDecimal("0.02"); // 2% per month



    public PurchaseService(PurchaseRepository purchaseRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           EmiCardRepository emiCardRepository, InstallmentScheduleRepository installmentScheduleRepository, TransactionRepository transactionRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.emiCardRepository = emiCardRepository;
        this.installmentScheduleRepository = installmentScheduleRepository;
        this.transactionRepository = transactionRepository;
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


    private void generateInstallmentSchedule(Purchase purchase) {
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
    public Transaction payInstallment(int installmentId, int userId) {
        InstallmentSchedule inst = installmentScheduleRepository.findById(installmentId).orElseThrow();
        if (inst.getPurchase().getUser().getId() != userId)
            throw new RuntimeException("Unauthorized");

        if (inst.getPaymentStatus() == InstallmentSchedule.PaymentStatus.Paid)
            throw new RuntimeException("Already paid");

        //if penalty amount then add penalty amount too
        BigDecimal amt = inst.getInstallmentAmount()
                .add(Optional.ofNullable(inst.getPenaltyAmount()).orElse(BigDecimal.ZERO));


        Transaction txn = new Transaction();
        txn.setPurchase(inst.getPurchase());
        txn.setAmountPaid(amt);
        txn.setTransactionType(Transaction.TransactionType.INSTALLMENT);
        txn.setTransactionDate(LocalDate.now());
        transactionRepository.save(txn);

        inst.setPaymentStatus(InstallmentSchedule.PaymentStatus.Paid);
        inst.setPaidTransaction(txn);
        installmentScheduleRepository.save(inst);

        return txn;
    }




    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight
    public int updateOverdueInstallments() {
        int updated = installmentScheduleRepository.markOverdueInstallments(InstallmentSchedule.PaymentStatus.Overdue, InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
        System.out.println("Updated " + updated + " overdue installments");
        return updated;
    }

    public List<InstallmentSchedule> getOverdueInstallments() {
//        System.out.println("HIII");
//        System.out.println(installmentScheduleRepository.findPendingBefore(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now()));

        return installmentScheduleRepository.findByPaymentStatus(InstallmentSchedule.PaymentStatus.Overdue);
    }

    public List<InstallmentSchedule> findPendingFirst() {
        System.out.println("HIII");
        System.out.println(installmentScheduleRepository.findPendingBefore(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now()));

        return installmentScheduleRepository.findPendingBefore(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
    }


    @Transactional
    public void calculatePenalties() {
        List<InstallmentSchedule> overdueSchedules =  installmentScheduleRepository.findOverdueSchedules(InstallmentSchedule.PaymentStatus.Pending, LocalDate.now());
        System.out.println(overdueSchedules.size());

        for (InstallmentSchedule schedule : overdueSchedules) {
            long daysOverdue = ChronoUnit.DAYS.between(schedule.getDueDate(), LocalDate.now());

            // Grace period
            int graceDays = 5; // you can make it configurable
            if (daysOverdue <= graceDays) {
                schedule.setPenaltyAmount(BigDecimal.valueOf(0));
                continue;
            }

            // Exponential penalty slab logic
            BigDecimal installmentAmout = schedule.getInstallmentAmount();
            BigDecimal penalty = calculateExponentialPenalty(daysOverdue - graceDays, installmentAmout);
            schedule.setPenaltyAmount(penalty);
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
        // Slab-wise daily rates
        BigDecimal rate;
        if (days <= 10) rate = new BigDecimal("0.005");       // 0.5% per day
        else if (days <= 20) rate = new BigDecimal("0.008");  // 0.8% per day
        else rate = new BigDecimal("0.0084");                 // 0.84% per day

        BigDecimal base = BigDecimal.ONE.add(rate);

        // Use MathContext for precision
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);

        BigDecimal multiplier = base.pow((int) days, mc);

        // Penalty = principal * ((1 + rate)^days - 1)
        return principal.multiply(multiplier.subtract(BigDecimal.ONE), mc);
    }

    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight run penalty recalculation
    public void runPenaltyCalculation() {
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





