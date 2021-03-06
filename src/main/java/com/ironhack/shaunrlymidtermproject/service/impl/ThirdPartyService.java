package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Account;
import com.ironhack.shaunrlymidtermproject.dao.ThirdParty;
import com.ironhack.shaunrlymidtermproject.repository.*;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ThirdPartyService implements IThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingsRepository savingsRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Override
    public void update(Long id, ThirdParty thirdParty) throws Exception {
        Optional<ThirdParty> foundThirdParty = thirdPartyRepository.findById(id);
        if (foundThirdParty.isEmpty()) {
            throw new AccountNotFoundException("No Third Party Account with that ID");
        }
        if (thirdParty.getBalance() != null) {
            try {
                foundThirdParty.get().setBalance(thirdParty.getBalance());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance Formatted Incorrectly");
            }
        }
        if (thirdParty.getName() != null) {
            try {
                foundThirdParty.get().setName(thirdParty.getName());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name Formatted Incorrectly");
            }
        }
        if (thirdParty.getHashedKey() != null) {
            try {
                foundThirdParty.get().setHashedKey(thirdParty.getHashedKey());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Key Formatted Incorrectly");
            }
        }
        thirdPartyRepository.save(foundThirdParty.get());
    }

    public String moneyTransfer(ThirdParty thirdParty, String hashedKey, BigDecimal transferAmount, Long targetId, String secretKey) {
        if (thirdParty.getBalance().getAmount().compareTo(transferAmount) == -1) {
            return "Balance of account below amount requested to transfer";
        }
        Optional<? extends Account> targetAccount = checkingRepository.findById(targetId);
        targetAccount = studentCheckingRepository.findById(targetId);
        targetAccount = savingsRepository.findById(targetId);
        targetAccount = checkingRepository.findById(targetId);

        if (targetAccount.isEmpty() || !targetAccount.get().getSecretKey().equals(secretKey)) {
            return "That account couldn't be found.";
        }
        thirdParty.paymentOut(transferAmount);
        targetAccount.get().paymentIn(transferAmount);
        return "Transfer Successful";
    }


}
