package com.ironhack.shaunrlymidtermproject.controller.interfaces;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;

import java.util.List;

public interface ICreditCardController {
    public void newCreditCard(CreditCard creditCard);
    public List<CreditCard> getAllCreditCards();
    public void update(Long id, CreditCard creditCard);
    public void deleteById(Long id);
    public CreditCard transfer(Long id, TransferDTO transferDTO);
}
