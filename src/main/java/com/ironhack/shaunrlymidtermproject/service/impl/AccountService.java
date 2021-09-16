package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Account;
import com.ironhack.shaunrlymidtermproject.dao.AccountHolder;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    CheckingRepository checkingRepository;
    CreditCardRepository creditCardRepository;
    SavingsRepository savingsRepository;
    StudentCheckingRepository studentCheckingRepository;

    public Account updateSuper(Long id, List<? extends Account> accounts){
        if (accounts.get(1).getBalance() != null){
            try {
                accounts.get(0).setBalance(accounts.get(1).getBalance());
            } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance formatted incorrectly.");
            }
        }
        if (accounts.get(1).getPrimaryOwner() != null){
            try {
                accounts.get(0).setPrimaryOwner(accounts.get(1).getPrimaryOwner());
            } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Primary Owner formatted incorrectly.");
            }
        }
        if (accounts.get(1).getSecondaryOwner() != null){
            try {
                accounts.get(0).setSecondaryOwner(accounts.get(1).getSecondaryOwner());
            } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Secondary Owner formatted incorrectly.");
            }
        }
        if (accounts.get(1).getStatus() != null){
            try {
                accounts.get(0).setStatus(accounts.get(1).getStatus());
            } catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status formatted incorrectly.");
            }
        }
        return accounts.get(0);
    }
}
