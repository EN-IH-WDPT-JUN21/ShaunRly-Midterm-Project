package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.dao.Account;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class AccountsController {

    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;


//    @GetMapping("/myaccounts")
//    @ResponseStatus(HttpStatus.OK)

}
