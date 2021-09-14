package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SavingsTest {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    AccountHolder accountHolder1;
    Savings savings1;

    @BeforeEach
    void SetUp(){
        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder1);

        savings1 = new Savings(new Money(BigDecimal.valueOf(10000)), accountHolder1, null);
        savingsRepository.save(savings1);
    }

    @AfterEach
    void tearDown(){
        savingsRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void testToString() {
        String testString = savingsRepository.getById(savings1.getId()).toString();
        assertTrue(testString.contains("10000"));
    }

    @Test
    void paymentOut_NoFee() {
        BigDecimal currBalance = savingsRepository.getById(savings1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        Savings savingsAcc = savingsRepository.getById(savings1.getId());
        savingsAcc.paymentOut(BigDecimal.valueOf(100));
        savingsRepository.save(savingsAcc);

        BigDecimal currBalance2 = savingsRepository.getById(savings1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(9900)).getAmount(), currBalance2);
    }

    @Test
    void paymentOut_FeeApplied(){
        BigDecimal currBalance = savingsRepository.getById(savings1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        //savingsRepository.getById(savings1.getId()).paymentOut(BigDecimal.valueOf(100));

        Savings savingsAcc = savingsRepository.getById(savings1.getId());
        savingsAcc.paymentOut(BigDecimal.valueOf(9050));
        savingsRepository.save(savingsAcc);

        BigDecimal currBalance2 = savingsRepository.getById(savings1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(10000)
                .subtract(BigDecimal.valueOf(9050))
                .subtract(savings1.getPenaltyFee())).getAmount(), currBalance2);
    }

    @Test
    void paymentIn_NoInterestApplied(){
        BigDecimal currBalance = savingsRepository.getById(savings1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        Savings savingsAcc = savingsRepository.getById(savings1.getId());
        savingsAcc.paymentIn(BigDecimal.valueOf(100));
        savingsRepository.save(savingsAcc);

        BigDecimal currBalance2 = savingsRepository.getById(savings1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(10100)).getAmount(), currBalance2);
    }

    @Test
    void paymentIn_InterestApplied(){
        BigDecimal currBalance = savingsRepository.getById(savings1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        Savings savingsAcc = savingsRepository.getById(savings1.getId());
        System.out.println(savingsAcc.getInterestRate());
        savingsAcc.setDateOfLastInterestPayment(LocalDate.now().minusYears(1).minusDays(1));
        savingsAcc.paymentIn(new BigDecimal("100"));
        savingsRepository.save(savingsAcc);

        BigDecimal currBalance2 = savingsRepository.getById(savings1.getId()).getBalance().getAmount();

        System.out.println(savingsAcc.getInterestRate());
        System.out.println(currBalance2);

        assertEquals(new Money(BigDecimal.valueOf(10000)
                .add(BigDecimal.valueOf(100))
                .multiply(savings1.getInterestRate())).getAmount(),
                currBalance2);
    }
}