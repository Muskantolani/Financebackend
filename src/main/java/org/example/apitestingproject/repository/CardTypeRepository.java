package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.CardType;
import org.springframework.data.repository.CrudRepository;

public interface CardTypeRepository extends CrudRepository<CardType,Integer> {
}
