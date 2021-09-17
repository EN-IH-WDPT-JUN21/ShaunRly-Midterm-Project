package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.shaunrlymidtermproject.dao.*;
import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountHolder accountHolder1;
    private Account checking1;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder1);

        checking1 = Account.createChecking(new Money(new BigDecimal("100")), accountHolder1, null);
        checkingRepository.save((Checking) checking1);
    }

    @AfterEach
    void tearDown(){
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void update_Valid_Created() throws Exception{
        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        Checking newCheckingAccount = new Checking(new Money(new BigDecimal("1000")), accountHolder1, accountHolder2);
        checkingRepository.save(newCheckingAccount);

        assertFalse(checkingRepository.getById(checking1.getId()).getSecondaryOwner().getName().contains("Huey"));

        String body = objectMapper.writeValueAsString(newCheckingAccount);

        MvcResult result = mockMvc.perform(
                put("/checking/"+checking1.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertTrue(checkingRepository.getById(checking1.getId()).getSecondaryOwner().getName().contains("Huey"));
    }
}