package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.interfaces.ICheckingController;
import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    ICheckingService checkingService;

    @PostMapping("/checking/new")
    @ResponseStatus(HttpStatus.OK)
    public void newCheckingAccount(@RequestBody @Valid Checking checking){
        checkingRepository.save(checking);
    }

    @PutMapping("/checking/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid Checking checking){
        checkingService.update(id, checking);
    }

    @GetMapping("/checking/admin/getall")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> getAllCheckingAccounts(){
        return checkingRepository.findAll();
    }

    @GetMapping("/checking/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking getById(@PathVariable(name = "id") Long id, Principal principal){
        if (checkingRepository.findById(id).get().getPrimaryOwner().getUsername().equals(principal.getName())) {
            return checkingRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    @DeleteMapping("/checking/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        checkingRepository.deleteById(id);
    }
}
