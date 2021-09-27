package com.ironhack.shaunrlymidtermproject.service.interfaces;

import com.ironhack.shaunrlymidtermproject.dao.Account;
import com.ironhack.shaunrlymidtermproject.dao.Checking;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Account updateSuper(Long id, List<? extends Account> Accounts);
    Account moneyTransfer(Account fromAccount, BigDecimal transferAmount, Long targetId, String name);
}
