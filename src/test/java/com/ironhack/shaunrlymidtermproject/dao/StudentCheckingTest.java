package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentCheckingTest {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    AccountHolder accountHolder1;
    StudentChecking studentChecking1;

    @BeforeEach
    void SetUp(){
        accountHolder1 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder1);

        studentChecking1 = new StudentChecking(new Money(BigDecimal.valueOf(100)), accountHolder1, null);
        studentCheckingRepository.save(studentChecking1);
    }

    @AfterEach
    void TearDown(){
        studentCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void canUpgradeNow_No() {
        StudentChecking huey = studentCheckingRepository.getById(studentChecking1.getId());
        assertTrue(huey.canUpgrade().contains("false"));
    }

    @Test
    void canUpgradeNow_Yes_OldEnough() {
        StudentChecking huey = studentCheckingRepository.getById(studentChecking1.getId());
        assertTrue(huey.canUpgrade().contains("false"));

        huey.getPrimaryOwner().setDateOfBirth(LocalDate.of(1990, 1, 10));
        assertTrue(huey.canUpgrade().contains("true"));
    }
}