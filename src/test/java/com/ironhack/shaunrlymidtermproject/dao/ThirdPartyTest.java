package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.repository.ThirdPartyRepository;
import com.ironhack.shaunrlymidtermproject.utils.Hasher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ThirdPartyTest {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    ThirdParty thirdParty1;

    @BeforeEach
    void setUp(){
        thirdParty1 = new ThirdParty("12345", "BT", new Money(new BigDecimal("1000")));
        thirdPartyRepository.save(thirdParty1);
    }

    @AfterEach
    void tearDown(){
        thirdPartyRepository.deleteAll();
    }

    @Test
    void hash_Equivalence_Testing(){
        ThirdParty thirdParty1 = new ThirdParty("12345", "BT", new Money(new BigDecimal("1000")));
        assertTrue(Hasher.pwdEnconder.matches("12345", thirdParty1.getHashedKey()));
    }

    @Test
    void paymentIn() {
        assertEquals(new BigDecimal("1000.00"),
                thirdPartyRepository.getById(thirdParty1.getId()).getBalance().getAmount());

        thirdParty1 = thirdPartyRepository.getById(thirdParty1.getId());
        thirdParty1.paymentIn(new BigDecimal("100"));
        thirdPartyRepository.save(thirdParty1);

        assertEquals(new BigDecimal("1100.00"),
                thirdPartyRepository.getById(thirdParty1.getId()).getBalance().getAmount());
    }

    @Test
    void paymentOut() {
        assertEquals(new BigDecimal("1000.00"),
                thirdPartyRepository.getById(thirdParty1.getId()).getBalance().getAmount());

        thirdParty1 = thirdPartyRepository.getById(thirdParty1.getId());
        thirdParty1.paymentOut(new BigDecimal("100"));
        thirdPartyRepository.save(thirdParty1);

        assertEquals(new BigDecimal("900.00"),
                thirdPartyRepository.getById(thirdParty1.getId()).getBalance().getAmount());
    }
}