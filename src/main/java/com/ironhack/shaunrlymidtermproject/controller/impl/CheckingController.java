package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.interfaces.ICheckingController;
import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CheckingController implements ICheckingController {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    ICheckingService checkingService;

    @PostMapping("/checking/newaccount")

    @PutMapping("/checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid Checking checking){
        checkingService.update(id, checking);
    }

    @GetMapping("/checking/admin/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Checking> getAllCheckingAccounts(){
        return checkingRepository.findAll();
    }

    @GetMapping("/checking/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Checking getById(@PathVariable(name = "id") Long id){
        return checkingRepository.findById(id).orElse(null);
    }
}
