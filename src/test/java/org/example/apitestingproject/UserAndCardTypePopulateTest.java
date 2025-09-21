


package org.example.apitestingproject;

import org.example.apitestingproject.entities.Bank;
import org.example.apitestingproject.entities.CardType;
import org.example.apitestingproject.entities.User;
import org.example.apitestingproject.repository.BankRepository;
import org.example.apitestingproject.repository.CardTypeRepository;
import org.example.apitestingproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class UserAndCardTypePopulateTest {

    @Autowired private UserRepository userRepository;
    @Autowired private CardTypeRepository cardTypeRepository;
    @Autowired private BankRepository bankRepository;

    @Test
    void populateUsersAndCards() {
        // --- Insert Card Types ---
        CardType gold = new CardType();
        gold.setName(CardType.Name.GOLD);
        gold.setDefaultLimit(new BigDecimal("300000"));
        gold.setJoiningFee(new BigDecimal("2000"));

        CardType titanium = new CardType();
        titanium.setName(CardType.Name.TITANIUM);
        titanium.setDefaultLimit(new BigDecimal("500000"));
        titanium.setJoiningFee(new BigDecimal("5000"));

        gold = cardTypeRepository.save(gold);
        titanium = cardTypeRepository.save(titanium);

        // --- Pick Banks from previous BankPopulateTest ---
        Bank sbi = bankRepository.findById(1).orElseThrow();
        Bank hdfc = bankRepository.findById(2).orElseThrow();
        Bank icici = bankRepository.findById(3).orElseThrow();

        // --- Users ---
        // 1. Super Admin
        User admin = new User();
        admin.setName("Arjun Mehta");
        admin.setDateOfBirth(LocalDate.of(1985, 1, 10));
        admin.setPhoneNo("9000000001");
        admin.setEmail("arjun.admin@example.com");
        admin.setUsername("admin01");
        admin.setPassword("securepass");
        admin.setPasswordUpdateTimestamp(LocalDateTime.now());
        admin.setAddress("Bangalore, India");
        admin.setRole(User.Role.Super_Admin);
        admin.setPreferredCardType(titanium);
        admin.setBank(sbi);
        admin.setSavingsAccountNo("111111111111");
        admin.setJoiningFeePaid(User.YesNo.Yes);
        admin.setAccountStatus(User.AccountStatus.Activated);

        // 2. Sub Admin 1
        User sub1 = new User();
        sub1.setName("Priya Nair");
        sub1.setDateOfBirth(LocalDate.of(1992, 6, 25));
        sub1.setPhoneNo("9000000002");
        sub1.setEmail("priya.subadmin@example.com");
        sub1.setUsername("subadmin01");
        sub1.setPassword("password1");
        sub1.setPasswordUpdateTimestamp(LocalDateTime.now());
        sub1.setAddress("Chennai, India");
        sub1.setRole(User.Role.Sub_Admin);
        sub1.setPreferredCardType(gold);
        sub1.setBank(hdfc);
        sub1.setSavingsAccountNo("222222222222");
        sub1.setJoiningFeePaid(User.YesNo.No);
        sub1.setAccountStatus(User.AccountStatus.Pending);

        // 3. Sub Admin 2
        User sub2 = new User();
        sub2.setName("Rohit Singh");
        sub2.setDateOfBirth(LocalDate.of(1993, 3, 18));
        sub2.setPhoneNo("9000000003");
        sub2.setEmail("rohit.subadmin@example.com");
        sub2.setUsername("subadmin02");
        sub2.setPassword("password2");
        sub2.setPasswordUpdateTimestamp(LocalDateTime.now());
        sub2.setAddress("Hyderabad, India");
        sub2.setRole(User.Role.Sub_Admin);
        sub2.setPreferredCardType(titanium);
        sub2.setBank(icici);
        sub2.setSavingsAccountNo("333333333333");
        sub2.setJoiningFeePaid(User.YesNo.Yes);
        sub2.setAccountStatus(User.AccountStatus.Activated);

        // 4. User 1 (with GOLD)
        User user1 = new User();
        user1.setName("Karan Patel");
        user1.setDateOfBirth(LocalDate.of(1998, 7, 12));
        user1.setPhoneNo("9000000004");
        user1.setEmail("karan.user@example.com");
        user1.setUsername("user01");
        user1.setPassword("userpass1");
        user1.setPasswordUpdateTimestamp(LocalDateTime.now());
        user1.setAddress("Pune, India");
        user1.setRole(User.Role.User);
        user1.setPreferredCardType(gold);
        user1.setBank(sbi);
        user1.setSavingsAccountNo("444444444444");
        user1.setJoiningFeePaid(User.YesNo.No);
        user1.setAccountStatus(User.AccountStatus.Pending);

        // 5. User 2 (with TITANIUM)
        User user2 = new User();
        user2.setName("Neha Gupta");
        user2.setDateOfBirth(LocalDate.of(1999, 11, 2));
        user2.setPhoneNo("9000000005");
        user2.setEmail("neha.user@example.com");
        user2.setUsername("user02");
        user2.setPassword("userpass2");
        user2.setPasswordUpdateTimestamp(LocalDateTime.now());
        user2.setAddress("Lucknow, India");
        user2.setRole(User.Role.User);
        user2.setPreferredCardType(titanium);
        user2.setBank(hdfc);
        user2.setSavingsAccountNo("555555555555");
        user2.setJoiningFeePaid(User.YesNo.Yes);
        user2.setAccountStatus(User.AccountStatus.Activated);

        // 6. User 3 (with TITANIUM)
        User user3 = new User();
        user3.setName("Anjali Verma");
        user3.setDateOfBirth(LocalDate.of(2000, 12, 20));
        user3.setPhoneNo("9000000006");
        user3.setEmail("anjali.user@example.com");
        user3.setUsername("user03");
        user3.setPassword("userpass3");
        user3.setPasswordUpdateTimestamp(LocalDateTime.now());
        user3.setAddress("Jaipur, India");
        user3.setRole(User.Role.User);
        user3.setPreferredCardType(titanium);
        user3.setBank(icici);
        user3.setSavingsAccountNo("666666666666");
        user3.setJoiningFeePaid(User.YesNo.No);
        user3.setAccountStatus(User.AccountStatus.Rejected);

        // Save all
        userRepository.save(admin);
        userRepository.save(sub1);
        userRepository.save(sub2);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}