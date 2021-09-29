package com.ironhack.shaunrlymidtermproject.controller.interfaces;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.Checking;

import java.util.List;

public interface ICheckingController {
    public void newCheckingAccount(Checking checking);
    public List<Checking> getAllCheckingAccounts();
    public void update(Long id, Checking checking);
    public void deleteById(Long id);
    public Checking transfer(Long id, TransferDTO transferDTO);

}
