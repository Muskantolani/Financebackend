package org.example.apitestingproject;

import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.repository.EmiCardRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.example.apitestingproject.repository.CardTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class EmiCardTest {

    @Autowired
    private EmiCardRepository emiCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Test
    public void populateEmiCards() {
        // User 4 → CardType 1
        EmiCard card1 = new EmiCard();
        card1.setUser(userRepository.findById(4).orElseThrow());
        card1.setCardType(cardTypeRepository.findById(1).orElseThrow());
        card1.setCardNumber("4111111111110004");
        card1.setValidTill(LocalDate.now().plusYears(3));
        card1.setActivationStatus(EmiCard.ActivationStatus.Active);
        card1.setAvailableLimit(card1.getCreditLimit()); // = 30000
        emiCardRepository.save(card1);

        // User 5 → CardType 2
        EmiCard card2 = new EmiCard();
        card2.setUser(userRepository.findById(5).orElseThrow());
        card2.setCardType(cardTypeRepository.findById(2).orElseThrow());
        card2.setCardNumber("4111111111110005");
        card2.setValidTill(LocalDate.now().plusYears(3));
        card2.setActivationStatus(EmiCard.ActivationStatus.Active);
        card2.setAvailableLimit(card2.getCreditLimit()); // = 50000
        emiCardRepository.save(card2);

        // User 6 → CardType 2
        EmiCard card3 = new EmiCard();
        card3.setUser(userRepository.findById(6).orElseThrow());
        card3.setCardType(cardTypeRepository.findById(2).orElseThrow());
        card3.setCardNumber("4111111111110006");
        card3.setValidTill(LocalDate.now().plusYears(3));
        card3.setActivationStatus(EmiCard.ActivationStatus.Active);
        card3.setAvailableLimit(card3.getCreditLimit()); // = 50000
        emiCardRepository.save(card3);
    }
}