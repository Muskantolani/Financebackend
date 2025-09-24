package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.entities.EmiCard;

import java.util.List;
import java.util.Optional;

public interface EmiCardService {
    Iterable<EmiCard> fetchAllEmiCards();

    Optional<EmiCard> fetchEmiCardById(Integer id);

    void insertEmiCard(EmiCard emiCard);

    boolean checkEmiCard(EmiCard emiCard);

    void updateEmiCard(EmiCard emiCard);

    void deleteEmiCard(EmiCard emiCard);

    List<EmiCard> fetchEmiCardsByStatus(String status);

    Integer getCountOfType(CardType type);
    long totalEmiCardCount();

}
