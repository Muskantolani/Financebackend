package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.CardType;

import java.util.List;
import java.util.Optional;

public interface CardTypeService {
    Iterable<CardType> fetchAllCardType();

    Optional<CardType> fetchCardTypeById(Integer id);

    void insertCardType(CardType cardType);

    boolean checkCardType(CardType cardType);

    void updateCardType(CardType cardType);

    void deleteCardType(CardType cardType);

}
