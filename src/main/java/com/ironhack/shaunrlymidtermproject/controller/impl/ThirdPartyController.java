package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.dao.ThirdParty;
import com.ironhack.shaunrlymidtermproject.repository.ThirdPartyRepository;
import com.ironhack.shaunrlymidtermproject.service.impl.ThirdPartyService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class ThirdPartyController {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    IThirdPartyService thirdPartyService;

    @PostMapping("/thirdparty/admin/new")
    @ResponseStatus(HttpStatus.OK)
    public void newCheckingAccount(@RequestBody @Valid ThirdParty thirdParty){
        thirdPartyRepository.save(thirdParty);
    }

    @PutMapping("/thirdparty/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid ThirdParty thirdParty) throws Exception {
        thirdPartyService.update(id, thirdParty);
    }

    @GetMapping("/thirdparty/admin/getall")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> getAllCheckingAccounts(){
        return thirdPartyRepository.findAll();
    }

    @GetMapping("/thirdparty/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdParty getById(@PathVariable(name = "id") Long id, Principal principal){
        if (thirdPartyRepository.findById(id).get().getName().equals(principal.getName())) {
            return thirdPartyRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    @DeleteMapping("/thirdparty/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id){
        thirdPartyRepository.deleteById(id);
    }

    @PatchMapping("/thirdparty/account/transfer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getById(@PathVariable(name = "id") Long id, Principal principal,
                          @RequestBody String hashedKey, @RequestBody BigDecimal transferAmount,
                          @RequestBody Long targetId, @RequestBody String secretKey){
        if (thirdPartyRepository.findById(id).get().getName().equals(principal.getName())) {
            return thirdPartyService.moneyTransfer(thirdPartyRepository.findById(id).get(), hashedKey, transferAmount, targetId, secretKey);
        } else {
            return null;
        }
    }
}
