package org.example.apitestingproject.service;

import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.repository.EmiCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmiCardServiceImpl implements EmiCardService {

    @Autowired
    private EmiCardRepository emiCardRepository;

    @Override
    public Iterable<EmiCard> fetchAllEmiCards() {
        return emiCardRepository.findAll();
    }

    @Override
    public Optional<EmiCard> fetchEmiCardById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void insertEmiCard(EmiCard emiCard) {
        emiCardRepository.save(emiCard);
    }

    @Override
    public boolean checkEmiCard(EmiCard emiCard) {
        return emiCardRepository.existsById(emiCard.getId());
    }

    @Override
    public void updateEmiCard(EmiCard emiCard) {
        emiCardRepository.save(emiCard);
    }

    @Override
    public void deleteEmiCard(EmiCard emiCard) {
        emiCardRepository.delete(emiCard);
    }

    @Override
    public List<EmiCard> fetchEmiCardsByStatus(String status) {
        return emiCardRepository.findByActivationStatus(status);
    }

    @Override
    public long totalEmiCardCount() {
        return emiCardRepository.count();
    }

    @Override
    public Integer getCountOfType(CardType type){
        return emiCardRepository.countByCardType(type);
    }

}
