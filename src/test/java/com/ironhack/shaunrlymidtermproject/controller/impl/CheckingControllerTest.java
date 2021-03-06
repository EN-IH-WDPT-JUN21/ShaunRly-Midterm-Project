package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.*;
import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.repository.UserRepository;
import com.ironhack.shaunrlymidtermproject.security.CustomUserDetails;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    IAccountService accountService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountHolder accountHolder1;
    private Account checking1;
    private User mockUser;
    private PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        accountHolder1 = new AccountHolder("Scrooge McDuck", LocalDate.of(1956, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolder1.setUsername("USER");
        accountHolderRepository.save(accountHolder1);

        checking1 = Account.createChecking(new Money(new BigDecimal("1000")), accountHolder1, null);
        checkingRepository.save((Checking) checking1);

    }

    @AfterEach
    void tearDown(){
        checkingRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void newCheckingAccount() throws Exception {
        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        Checking newCheckingAccount = new Checking(new Money(new BigDecimal("1000")), accountHolder2, null);

        assertEquals(1, checkingRepository.findAll().size());

        String body = objectMapper.writeValueAsString(newCheckingAccount);

        MvcResult result = mockMvc.perform(
                post("/checking/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(2, checkingRepository.findAll().size());

    }

    @Test
    void getAllCheckingAccounts() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/checking/admin/getall")).andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Scrooge McDuck"));
    }

    @Test
    void getById() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        mockUser = userRepository.save(new User("USER", pwdEnconder.encode("123456")));
        MvcResult result = mockMvc.perform(
                get("/checking/account/"+ checking1.getId())
                        .with(user(new CustomUserDetails(mockUser))))
                .andExpect(status().isOk()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("Scrooge McDuck"));
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
                put("/checking/admin/"+checking1.getId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertTrue(checkingRepository.getById(checking1.getId()).getSecondaryOwner().getName().contains("Huey"));
    }

    @Test
    void deleteById() throws Exception{
        assertEquals(1, checkingRepository.findAll().size());

        MvcResult result = mockMvc.perform(
                delete("/checking/admin/"+checking1.getId())).andExpect(status().isOk()).andReturn();

        assertEquals(0, checkingRepository.findAll().size());
    }

    @Test
    void transferMoney_Successful() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        mockUser = userRepository.save(new User("USER", pwdEnconder.encode("123456")));

        assertEquals(new BigDecimal("1000.00"), checkingRepository.getById(checking1.getId()).getBalance().getAmount());

        AccountHolder accountHolder2 = new AccountHolder("Huey McDuck", LocalDate.of(2002, 10, 24),
                new Address(1, "McDuck Manor", "Duckburg", "New York", "USA", "DU12 CK3"));
        accountHolderRepository.save(accountHolder2);

        Checking newCheckingAccount = new Checking(new Money(new BigDecimal("1000.00")), accountHolder1, accountHolder2);
        checkingRepository.save(newCheckingAccount);

        TransferDTO transfer = new TransferDTO(new BigDecimal("100"),
                newCheckingAccount.getId(), newCheckingAccount.getPrimaryOwner().getName());

        String body = objectMapper.writeValueAsString(transfer);

        MvcResult result = mockMvc.perform(
                patch("/checking/account/transfer/"+checking1.getId())
                        .with(user(new CustomUserDetails(mockUser)))
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("1100.00"), checkingRepository.getById(newCheckingAccount.getId()).getBalance().getAmount());

    }
}