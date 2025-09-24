package org.example.apitestingproject.repository;

import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByAccountStatus(User.AccountStatus accountStatus);
    long countUserByAccountStatus(User.AccountStatus status);
//    long countUserByCardType(CardType.name);

}