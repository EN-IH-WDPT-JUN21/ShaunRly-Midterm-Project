package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Savings;
import com.ironhack.shaunrlymidtermproject.repository.SavingsRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ISavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SavingsService implements ISavingsService {

    @Autowired
    SavingsRepository savingsRepository;

    @Autowired
    IAccountService accountService;

    public void update(Long id, Savings savings) {

        Optional<Savings> storedSavings = savingsRepository.findById(id);
        if (storedSavings.isPresent()) {
            Savings storedSuperUpdated = (Savings) accountService.updateSuper(id, List.of(storedSavings.get(), savings));
            if (savings.getMinimumBalance() != null) {
                try {
                    storedSuperUpdated.setMinimumBalance(savings.getMinimumBalance());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum Balance formatted incorrectly.");
                }
            }
            if (savings.getInterestRate() != null) {
                try {
                    storedSuperUpdated.setInterestRate(savings.getInterestRate());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Interest Rate formatted incorrectly.");
                }
            }
            savingsRepository.save(storedSuperUpdated);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tha Student Checking Account does not exists");
        }

    }
}
