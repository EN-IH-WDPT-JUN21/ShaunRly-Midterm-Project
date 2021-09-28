package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.shaunrlymidtermproject.dao.Money;
import com.ironhack.shaunrlymidtermproject.dao.ThirdParty;
import com.ironhack.shaunrlymidtermproject.dao.User;
import com.ironhack.shaunrlymidtermproject.repository.ThirdPartyRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    UserRepository userRepository;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ThirdParty thirdParty1;
    private User mockUser;
    private PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        thirdParty1 = new ThirdParty("abc", "McDuck Foundation",
                new Money(new BigDecimal("10000")));
        thirdParty1.setUsername("USER");
        thirdPartyRepository.save(thirdParty1);

        User user = new User("USER", "123456");
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown(){
        thirdPartyRepository.deleteAll();
    }

    @Test
    void newThirdPartyAccount() throws Exception{
        assertEquals(1, thirdPartyRepository.findAll().size());

        ThirdParty thirdParty2 = new ThirdParty("123", "Joe Blogs Blogs",
                new Money(new BigDecimal("100")));

        String body  = objectMapper.writeValueAsString(thirdParty2);

        MvcResult result = mockMvc.perform(
                post("/thirdparty/admin/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(2, thirdPartyRepository.findAll().size());
    }

    @Test
    void update() throws Exception{
        ThirdParty thirdParty2 = new ThirdParty("123", "Joe Blogs Blogs",
                new Money(new BigDecimal("100")));
        thirdPartyRepository.save(thirdParty2);

        assertFalse(thirdPartyRepository.getById(thirdParty1.getId()).getName().contains("Joe"));

        String body = objectMapper.writeValueAsString(thirdParty2);

        MvcResult result = mockMvc.perform(
                put("/thirdparty/admin/"+thirdParty1.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        assertTrue(thirdPartyRepository.getById(thirdParty1.getId()).getName().contains("Joe"));
    }

    @Test
    void getAllThirdPartyAccounts() throws Exception{
        MvcResult result = mockMvc.perform(
                get("/thirdparty/admin/getall")).andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("McDuck Foundation"));
    }

    @Test
    void getById() throws Exception{
        mockUser = userRepository.save(new User("USER", pwdEnconder.encode("123456")));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        MvcResult result = mockMvc.perform(
                get("/thirdparty/account/"+ thirdParty1.getId())
                        .with(user(new CustomUserDetails(mockUser))))
                .andExpect(status().isOk()).andReturn();


        assertTrue(result.getResponse().getContentAsString().contains("McDuck Foundation"));
    }

    @Test
    void deleteById() throws Exception{
        assertEquals(1, thirdPartyRepository.findAll().size());

        MvcResult result = mockMvc.perform(
                delete("/thirdparty/admin/"+thirdParty1.getId())).andExpect(status().isOk()).andReturn();

        assertEquals(0, thirdPartyRepository.findAll().size());
    }

}