package org.example.apitestingproject.service;

import org.example.apitestingproject.DTO.EmiOptionResponse;
import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.entities.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class EmiPreviewService {
    private final UserRiskService userRiskService;

    public EmiPreviewService(UserRiskService userRiskService) {
        this.userRiskService = userRiskService;
    }

    public List<EmiOptionResponse> generateEmiOptions(BigDecimal productPrice, EmiCard card) {
        List<Integer> tenures = List.of(3, 6, 9, 12);

        return tenures.stream()
                .map(months -> buildOption(productPrice, card, months))
                .toList();
    }



    private EmiOptionResponse buildOption(BigDecimal productPrice, EmiCard card, int months) {
        BigDecimal processingFee = calculateProcessingFee(productPrice, card, months);
        BigDecimal totalPayable = productPrice.add(processingFee);
        BigDecimal monthlyInstallment = totalPayable.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);

        EmiOptionResponse option = new EmiOptionResponse();
        option.setTenureMonths(months);
        option.setProcessingFee(processingFee);
        option.setTotalPayable(totalPayable);
        option.setMonthlyInstallment(monthlyInstallment);

        return option;
    }

    private BigDecimal calculateProcessingFee(BigDecimal price, EmiCard card, int months) {
        // Determine user risk profile dynamically
        User user = card.getUser();
        User.UserRiskProfile riskProfile = userRiskService.evaluateUser(user.getId());

        String cardType = card.getCardType().getName().name();
        BigDecimal rate;

        if ("GOLD".equalsIgnoreCase(cardType)) {
            switch (months) {
                case 3 -> rate = new BigDecimal("0.015"); // 1.5%
                case 6 -> rate = new BigDecimal("0.020"); // 2%
                case 9 -> rate = new BigDecimal("0.025"); // 2.5%
                case 12 -> rate = new BigDecimal("0.030"); // 3%
                default -> rate = new BigDecimal("0.02");
            }
        } else { // TITANIUM or others
            switch (months) {
                case 3 -> rate = new BigDecimal("0.010"); // 1%
                case 6 -> rate = new BigDecimal("0.015"); // 1.5%
                case 9 -> rate = new BigDecimal("0.020"); // 2%
                case 12 -> rate = new BigDecimal("0.025"); // 2.5%
                default -> rate = new BigDecimal("0.015");
            }
        }

        // Adjust rate based on risk profile
        if (riskProfile == User.UserRiskProfile.GOOD) {
            rate = rate.multiply(new BigDecimal("0.9")); // 10% discount
            System.out.println("User is good");
        } else if (riskProfile == User.UserRiskProfile.BAD) {
            rate = rate.multiply(new BigDecimal("1.5")); // 50% increase
            System.out.println("user is bad");
        }

        return price.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

}
