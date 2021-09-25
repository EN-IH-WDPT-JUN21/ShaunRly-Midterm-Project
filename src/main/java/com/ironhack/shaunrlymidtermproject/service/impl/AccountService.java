package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Account;
import com.ironhack.shaunrlymidtermproject.dao.AccountHolder;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService implements IAccountService {

    @Autowired
    CheckingRepository checkingRepository;
    CreditCardRepository creditCardRepository;
    SavingsRepository savingsRepository;
    StudentCheckingRepository studentCheckingRepository;

    private Set<? extends JpaRepository> repositoryList = Set.of(
            creditCardRepository, studentCheckingRepository,
            checkingRepository, savingsRepository);

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

    public String moneyTransfer(Account fromAccount, BigDecimal transferAmount, Long targetAccountId, String targetName){
        if(fromAccount.getBalance().getAmount().compareTo(transferAmount) == -1){
            return "Balance of account below amount requested to transfer";
        }
        Optional<? extends Account> targetAccount = checkingRepository.findById(targetAccountId);
        targetAccount = studentCheckingRepository.findById(targetAccountId);
        targetAccount = savingsRepository.findById(targetAccountId);
        targetAccount = checkingRepository.findById(targetAccountId);

        if(targetAccount.isEmpty()
                || !targetAccount.get().getPrimaryOwner().getName().equals(targetName)
                || !targetAccount.get().getSecondaryOwner().getName().equals(targetName)){
            return "That account couldn't be found.";
        }
        fromAccount.paymentOut(transferAmount);
        targetAccount.get().paymentIn(transferAmount);
        return "Transfer Successful";
    }
}
