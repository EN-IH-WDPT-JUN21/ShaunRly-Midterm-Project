package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditCardTest {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    AccountHolder accountHolder1;
    CreditCard creditCard1;

    @BeforeEach
    void SetUp(){
        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"), "user");
        accountHolderRepository.save(accountHolder1);

        creditCard1 = new CreditCard(new Money(BigDecimal.valueOf(50)), new BigDecimal("100"),
                accountHolder1, null, new BigDecimal("1.2"));
        creditCardRepository.save(creditCard1);
    }

    @Test
    void instantiateWith_CreditLimit_BelowMax(){
        CreditCard creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(50)), new BigDecimal("5000"),
                accountHolder1, null);
        assertEquals(new BigDecimal("5000"), creditCard2.getCreditLimit());
    }

    @Test
    void instantiateWith_CreditLimit_AboveMax(){
        CreditCard creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(50)), new BigDecimal("500000"),
                accountHolder1, null);
        assertEquals(new BigDecimal("100000"), creditCard2.getCreditLimit());
    }

    @Test
    void instantiateWith_InterestRate_AboveMin(){
        CreditCard creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(50)), accountHolder1, null,
                new BigDecimal("1.15"));
        assertEquals(new BigDecimal("1.15"), creditCard2.getInterestRate());
    }

    @Test
    void instantiateWith_InterestRate_BelowMin(){
        CreditCard creditCard2 = new CreditCard(new Money(BigDecimal.valueOf(50)), accountHolder1, null,
                new BigDecimal("1"));
        assertEquals(new BigDecimal("1.1"), creditCard2.getInterestRate());
    }

    @Test
    void creditPayment() {
        BigDecimal currBalance = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount(), currBalance);

        CreditCard creditCard = creditCardRepository.getById(creditCard1.getId());
        creditCard.creditPayment(new BigDecimal("25"));
        creditCardRepository.save(creditCard);

        BigDecimal currBalance1 = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("25")).getAmount(), currBalance1);
    }

    @Test
    void creditCardPurchase_OverCreditLimit_No() {
        BigDecimal currBalance = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount(), currBalance);

        CreditCard creditCard = creditCardRepository.getById(creditCard1.getId());
        String paymentMessage = creditCard.creditCardPurchase(new BigDecimal("25"));
        assertTrue(paymentMessage.contains("credit left on card"));
        creditCardRepository.save(creditCard);

        BigDecimal currBalance1 = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("75")).getAmount(), currBalance1);
    }

    @Test
    void creditCardPurchase_OverCreditLimit_Yes() {
        BigDecimal currBalance = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount(), currBalance);

        CreditCard creditCard = creditCardRepository.getById(creditCard1.getId());
        String paymentMessage = creditCard.creditCardPurchase(new BigDecimal("125"));
        assertTrue(paymentMessage.contains("Payment declined"));
        creditCardRepository.save(creditCard);

        BigDecimal currBalance1 = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount(), currBalance1);
    }

    @Test
    void creditCard_InterestApplied() {
        BigDecimal currBalance = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount(), currBalance);

        CreditCard creditCard = creditCardRepository.getById(creditCard1.getId());
        creditCard.setDateOfLastInterestPayment(LocalDate.now().minusMonths(1).minusDays(1));
        creditCardRepository.save(creditCard);

        BigDecimal currBalance1 = creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount();
        assertEquals(new Money(new BigDecimal("50")).getAmount().multiply(this.creditCard1.getInterestRate()).setScale(2), currBalance1);
    }
}