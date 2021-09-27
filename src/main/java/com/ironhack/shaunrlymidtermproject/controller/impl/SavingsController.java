package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.controller.interfaces.ISavingsController;
import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.dao.Savings;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ISavingsService;
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
public class SavingsController implements ISavingsController {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    ISavingsService savingsService;

    @Autowired
    IAccountService accountService;

    @PostMapping("/savings/new")
    @ResponseStatus(HttpStatus.OK)
    public void newCreditCard(@RequestBody @Valid Savings savings){
        savingsRepository.save(savings);
    }

    @PutMapping("/savings/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid Savings savings){
        savingsService.update(id, savings);
    }

    @GetMapping("/savings/admin/getall")
    @ResponseStatus(HttpStatus.OK)
    public List<Savings> getAllSavingsAccounts(){
        return savingsRepository.findAll();
    }

    @GetMapping("/savings/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings getById(@PathVariable(name = "id") Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (savingsRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername())) {
            return savingsRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    @DeleteMapping("/savings/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        savingsRepository.deleteById(id);
    }

    @PatchMapping("/savings/account/transfer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Savings transfer(@PathVariable(name = "id") Long id,
                             @RequestBody @Valid TransferDTO transferDTO){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (savingsRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername())) {
            Savings updatedTarget = (Savings) accountService.moneyTransfer(savingsRepository.findById(id).get(),
                    transferDTO.getTransferAmount(), transferDTO.getTargetId(), transferDTO.getTargetName());
            savingsRepository.save(updatedTarget);
            return updatedTarget;
        } else {
            return null;
        }
    }

}
