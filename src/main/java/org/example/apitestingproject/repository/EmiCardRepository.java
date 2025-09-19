package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.EmiCard;
import org.example.apitestingproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmiCardRepository extends JpaRepository<EmiCard,Integer> {
    Optional<EmiCard> findByUser(User user);
}
