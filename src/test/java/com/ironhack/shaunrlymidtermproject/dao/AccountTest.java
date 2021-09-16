package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountTest {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @AfterEach
    void tearDown() {
        studentCheckingRepository.deleteAll();
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void createChecking_AgeBelow24_StudentCheckingReturned() {
        AccountHolder accountHolder1 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder1);

        Account newCheckingAccount = Account.createChecking(new Money(new BigDecimal("100")), accountHolder1, null);
        assertEquals("StudentChecking", newCheckingAccount.getClass().getSimpleName());
    }

    @Test
    void createChecking_AgeAbove24_CheckingReturned(){
        AccountHolder accountHolder2 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        Account newCheckingAccount = Account.createChecking(new Money(new BigDecimal("100")), accountHolder2, null);
        assertEquals("Checking", newCheckingAccount.getClass().getSimpleName());
    }
}