package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.repository.CardTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardTypeServiceImpl implements CardTypeService {

    @Autowired
    private CardTypeRepository cardTypeRepository;

    @Override
    public Iterable<CardType> fetchAllCardType() {
        return cardTypeRepository.findAll();
    }

    @Override
    public Optional<CardType> fetchCardTypeById(Integer id) {
        return cardTypeRepository.findById(id);
    }

    @Override
    public void insertCardType(CardType cardType) {
        cardTypeRepository.save(cardType);
    }

    @Override
    public boolean checkCardType(CardType cardType) {
        return cardTypeRepository.existsById(cardType.getId());
    }

    @Override
    public void updateCardType(CardType cardType) {
        cardTypeRepository.save(cardType);
    }

    @Override
    public void deleteCardType(CardType cardType) {
        cardTypeRepository.delete(cardType);
    }

}
