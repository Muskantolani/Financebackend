package org.example.apitestingproject.DTO;


import java.math.BigDecimal;
import java.util.List;
public class PayInstallmentRequest {

        private int cardId;
        private List<Integer> installmentNos; // list of installment numbers to pay
        private BigDecimal amount;             // optional for partial payment

        // Getters and Setters
        public int getCardId() { return cardId; }
        public void setCardId(int cardId) { this.cardId = cardId; }

        public List<Integer> getInstallmentNos() { return installmentNos; }
        public void setInstallmentNos(List<Integer> installmentNos) { this.installmentNos = installmentNos; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
}