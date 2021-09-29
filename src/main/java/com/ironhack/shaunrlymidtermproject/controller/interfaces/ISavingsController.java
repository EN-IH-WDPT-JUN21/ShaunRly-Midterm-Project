package com.ironhack.shaunrlymidtermproject.controller.interfaces;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.Savings;

import java.util.List;

public interface ISavingsController {
    public void newSavings(Savings savings);
    public List<Savings> getAllSavingsAccounts();
    public void update(Long id, Savings savings);
    public void deleteById(Long id);
    public Savings transfer(Long id, TransferDTO transferDTO);
}
