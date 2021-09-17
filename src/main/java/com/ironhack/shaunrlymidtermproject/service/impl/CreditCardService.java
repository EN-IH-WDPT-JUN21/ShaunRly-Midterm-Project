package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    IAccountService accountService;

    public void update(Long id, CreditCard creditCard){

        Optional<CreditCard> storedCreditCard = creditCardRepository.findById(id);
        if (storedCreditCard.isPresent()){
            CreditCard storedSuperUpdated = (CreditCard) accountService.updateSuper(id, List.of(storedCreditCard.get(), creditCard));
            if(creditCard.getCreditLimit() != null){
                try {
                    storedSuperUpdated.setCreditLimit(creditCard.getCreditLimit());
                } catch (Exception e){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monthly Maintenance Fee formatted incorrectly.");
                }
            }
            if(creditCard.getInterestRate() != null){
                try {
                    storedSuperUpdated.setInterestRate(creditCard.getInterestRate());
                } catch (Exception e){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum Balance formatted incorrectly.");
                }
            }
            creditCardRepository.save(storedSuperUpdated);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That Checking Account does not exists");
        }

    }
}
