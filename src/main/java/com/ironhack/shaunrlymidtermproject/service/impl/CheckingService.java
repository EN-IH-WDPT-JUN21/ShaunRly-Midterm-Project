package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.Checking;
import com.ironhack.shaunrlymidtermproject.repository.CheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.ICheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CheckingService implements ICheckingService {

    @Autowired
    CheckingRepository checkingRepository;

    @Autowired
    IAccountService accountService;

    public void update(Long id, Checking checking) {

        Optional<Checking> storedChecking = checkingRepository.findById(id);
        if (storedChecking.isPresent()) {
            Checking storedSuperUpdated = (Checking) accountService.updateSuper(id, List.of(storedChecking.get(), checking));
            if (checking.getMonthlyMaintenanceFee() != null) {
                try {
                    storedSuperUpdated.setMonthlyMaintenanceFee(checking.getMonthlyMaintenanceFee());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monthly Maintenance Fee formatted incorrectly.");
                }
            }
            if (checking.getMinimumBalance() != null) {
                try {
                    storedSuperUpdated.setMinimumBalance(checking.getMinimumBalance());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum Balance formatted incorrectly.");
                }
            }
            checkingRepository.save(storedSuperUpdated);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That Checking Account does not exists");
        }

    }
}
