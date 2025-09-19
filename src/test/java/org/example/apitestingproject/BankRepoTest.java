package org.example.apitestingproject;

import org.example.apitestingproject.repository.BankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.apitestingproject.entities.Bank;


@SpringBootTest
public class BankRepoTest {
    @Autowired
    BankRepository bankRepository;

    @Test
    void populateIndianBanks() {
        Bank b1 = new Bank("State Bank of India", "SBIN");
        Bank b2 = new Bank("HDFC Bank", "HDFC");
        Bank b3 = new Bank("ICICI Bank", "ICIC");
        Bank b4 = new Bank("Axis Bank", "UTIB");
        Bank b5 = new Bank("Punjab National Bank", "PUNB");

        bankRepository.save(b1);
        bankRepository.save(b2);
        bankRepository.save(b3);
        bankRepository.save(b4);
        bankRepository.save(b5);
    }

}