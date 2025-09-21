package org.example.apitestingproject.DTO;

import java.math.BigDecimal;
import java.util.List;

public class EmiPreviewResponse {
    private int userId;
    private int productId;
    private String cardType;
    private BigDecimal productPrice;
    private List<EmiOptionResponse> emiOptions;
}
