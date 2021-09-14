package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheckingTest {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    AccountHolder accountHolder1;
    Checking checkingAccount1;

    @BeforeEach
    void SetUp(){
        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder1);

        checkingAccount1 = new Checking(new Money(BigDecimal.valueOf(10000)), accountHolder1, null);
        checkingRepository.save(checkingAccount1);
    }

    @Test
    void paymentOut_NoFee() {
        BigDecimal currBalance = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        Checking checkingAcc = checkingRepository.getById(checkingAccount1.getId());
        checkingAcc.paymentOut(BigDecimal.valueOf(100));
        checkingRepository.save(checkingAcc);

        BigDecimal currBalance2 = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(9900)).getAmount(), currBalance2);
    }

    @Test
    void paymentOut_FeeApplied(){
        BigDecimal currBalance = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        //savingsRepository.getById(savings1.getId()).paymentOut(BigDecimal.valueOf(100));

        Checking checkingAcc = checkingRepository.getById(checkingAccount1.getId());
        checkingAcc.paymentOut(BigDecimal.valueOf(9950));
        checkingRepository.save(checkingAcc);

        BigDecimal currBalance2 = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(10000)
                .subtract(BigDecimal.valueOf(9950))
                .subtract(checkingAccount1.getPenaltyFee())).getAmount(), currBalance2);
    }

    @Test
    void paymentIn_MonthlyMaintenanceFee_NotApplied() {
        BigDecimal currBalance = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        Checking checkingAcc = checkingRepository.getById(checkingAccount1.getId());
        checkingAcc.paymentIn(BigDecimal.valueOf(100));
        checkingRepository.save(checkingAcc);

        BigDecimal currBalance2 = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();

        assertEquals(new Money(BigDecimal.valueOf(10100)).getAmount(), currBalance2);
    }

    @Test
    void paymentIn_MonthlyMaintenanceFee_Applied() {
        //Assert original balance is instantiated value
        BigDecimal currBalance = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();
        assertEquals(new Money(BigDecimal.valueOf(10000)).getAmount(), currBalance);

        //Set date of last maintenance payment to current time minus 1 month to enable monthlyMaintenanceFee
        Checking checkingAcc = checkingRepository.getById(checkingAccount1.getId());
        checkingAcc.setDateOfLastMaintenancePayment(LocalDate.now().minusMonths(1).minusDays(1));
        checkingAcc.paymentIn(BigDecimal.valueOf(100));
        checkingRepository.save(checkingAcc);

        BigDecimal currBalance2 = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();

        //Assert new balance is equal to added amount minus maintenance fee
        assertEquals(new Money(BigDecimal.valueOf(10100)
                .subtract(checkingAcc.getMonthlyMaintenanceFee()))
                .getAmount(), currBalance2);

        Checking checkingAcc2 = checkingRepository.getById(checkingAccount1.getId());
        checkingAcc2.paymentIn(BigDecimal.valueOf(100));
        checkingRepository.save(checkingAcc2);

        BigDecimal currBalance3 = checkingRepository.getById(checkingAccount1.getId()).getBalance().getAmount();

        //Assert that date of last maintenance payment was reset in method call so maintenance fee is no longer applied
        //Check new balance is equal to last balance plus payment in only
        assertEquals(new Money(BigDecimal.valueOf(10100)
                .subtract(checkingAcc.getMonthlyMaintenanceFee())
                .add(new BigDecimal("100")))
                .getAmount(), currBalance3);
    }
}