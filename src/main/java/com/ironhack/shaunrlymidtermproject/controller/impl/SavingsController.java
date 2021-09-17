package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.interfaces.ISavingsController;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;
import com.ironhack.shaunrlymidtermproject.dao.Savings;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SavingsController implements ISavingsController {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    ISavingsService savingsService;

    @PutMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid Savings savings){
        savingsService.update(id, savings);
    }

}
