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

//    private Set<? extends JpaRepository> repositoryList = Set.of(
//            creditCardRepository, studentCheckingRepository,
//            checkingRepository, savingsRepository);
//
//    @GetMapping("/myaccounts")
//    @ResponseStatus(HttpStatus.OK)
//    public List<? extends Account> getAllUserAccounts(Principal principal){
//        List<Account> returnedAccounts = new ArrayList<>();
//        ArrayList<? extends Account> accountsList = (ArrayList<? extends Account>) checkingRepository.findAll();
//        for (JpaRepository repository : repositoryList){
//            List<? extends Account> accountList = repository.findAll();
//            for (Account account : accountList){
//                if(account.getPrimaryOwner().getUsername().equals(principal.getName())
//                        || account.getSecondaryOwner().getUsername().equals(principal.getName())){
//                    returnedAccounts.add(account);
//                }
//            }
//        }
//        return returnedAccounts;
//    }
}
