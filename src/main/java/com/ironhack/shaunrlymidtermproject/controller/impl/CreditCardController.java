package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.interfaces.ICreditCardController;
import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CreditCardController implements ICreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    ICreditCardService creditCardService;

    @PutMapping("/creditcard/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid CreditCard creditCard){
        creditCardService.update(id, creditCard);
    }
}
