package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.controller.interfaces.ICreditCardController;
import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;
import com.ironhack.shaunrlymidtermproject.repository.CreditCardRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class CreditCardController implements ICreditCardController {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    ICreditCardService creditCardService;

    @Autowired
    IAccountService accountService;

    @PostMapping("/credit/new")
    @ResponseStatus(HttpStatus.OK)
    public void newCreditCard(@RequestBody @Valid CreditCard creditCard){
        creditCardRepository.save(creditCard);
    }

    @PutMapping("/credit/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid CreditCard creditCard){
        creditCardService.update(id, creditCard);
    }

    @GetMapping("/credit/admin/getall")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAllCreditCards(){
        return creditCardRepository.findAll();
    }

    @GetMapping("/credit/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getById(@PathVariable(name = "id") Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (creditCardRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername())) {
            return creditCardRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    @DeleteMapping("/credit/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        creditCardRepository.deleteById(id);
    }

    @PatchMapping("/credit/account/transfer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard transfer(@PathVariable(name = "id") Long id,
                             @RequestBody @Valid TransferDTO transferDTO){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (creditCardRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername())) {
            CreditCard updatedTarget = (CreditCard) accountService.moneyTransfer(creditCardRepository.findById(id).get(),
                    transferDTO.getTransferAmount(), transferDTO.getTargetId(), transferDTO.getTargetName());
            creditCardRepository.save(updatedTarget);
            return updatedTarget;
        } else {
            return null;
        }
    }
}
