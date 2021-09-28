package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.*;
import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.repository.UserRepository;
import com.ironhack.shaunrlymidtermproject.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CreditCardControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    UserRepository userRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountHolder accountHolder1;
    private CreditCard creditCard1;
    private User mockUser;
    private PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolder1.setUsername("USER");
        accountHolderRepository.save(accountHolder1);

        creditCard1 = new CreditCard(new Money(new BigDecimal("1000")), accountHolder1, null);
        creditCardRepository.save(creditCard1);

        User user = new User("USER", "123456");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown(){
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void newCreditCardAccount() throws Exception {
        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        CreditCard newCreditCardAccount = new CreditCard(new Money(new BigDecimal("1000")), accountHolder2, null);

        assertEquals(1, creditCardRepository.findAll().size());

        String body = objectMapper.writeValueAsString(newCreditCardAccount);

        MvcResult result = mockMvc.perform(
                post("/credit/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(2, creditCardRepository.findAll().size());

    }

    @Test
    void getAllCreditCardAccounts() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/credit/admin/getall")).andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Scrooge McDuck"));
    }

    @Test
    void getById() throws Exception{
        mockUser = userRepository.save(new User("USER", pwdEnconder.encode("123456")));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        MvcResult result = mockMvc.perform(
                get("/credit/account/"+ creditCard1.getId())
                        .with(user(new CustomUserDetails(mockUser))))
                .andExpect(status().isOk()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("Scrooge McDuck"));
    }

    @Test
    void update_Valid_Created() throws Exception{
        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        CreditCard newCreditCardAccount = new CreditCard(new Money(new BigDecimal("1000")), accountHolder1, accountHolder2);
        creditCardRepository.save(newCreditCardAccount);

        assertFalse(creditCardRepository.getById(creditCard1.getId()).getSecondaryOwner().getName().contains("Huey"));

        String body = objectMapper.writeValueAsString(newCreditCardAccount);

        MvcResult result = mockMvc.perform(
                put("/credit/admin/"+creditCard1.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertTrue(creditCardRepository.getById(creditCard1.getId()).getSecondaryOwner().getName().contains("Huey"));
    }

    @Test
    void deleteById() throws Exception{
        assertEquals(1, creditCardRepository.findAll().size());

        MvcResult result = mockMvc.perform(
                delete("/credit/admin/"+creditCard1.getId())).andExpect(status().isOk()).andReturn();

        assertEquals(0, creditCardRepository.findAll().size());
    }

    @Test
    void transferMoney_Successful() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        mockUser = userRepository.save(new User("USER", pwdEnconder.encode("123456")));

        assertEquals(new BigDecimal("1000.00"), creditCardRepository.getById(creditCard1.getId()).getBalance().getAmount());

        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        CreditCard newCreditCard = new CreditCard(new Money(new BigDecimal("1000.00")), accountHolder1, accountHolder2);
        creditCardRepository.save(newCreditCard);

        TransferDTO transfer = new TransferDTO(new BigDecimal("100"),
                newCreditCard.getId(), newCreditCard.getPrimaryOwner().getName());

        String body = objectMapper.writeValueAsString(transfer);

        MvcResult result = mockMvc.perform(
                patch("/credit/account/transfer/"+creditCard1.getId())
                        .with(user(new CustomUserDetails(mockUser)))
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("900.00"), creditCardRepository.getById(newCreditCard.getId()).getBalance().getAmount());

    }
}
