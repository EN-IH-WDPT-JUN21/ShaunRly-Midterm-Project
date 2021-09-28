package com.ironhack.shaunrlymidtermproject.service.interfaces;

import com.ironhack.shaunrlymidtermproject.dao.Account;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    Account updateSuper(Long id, List<? extends Account> Accounts);

    Account moneyTransfer(Account fromAccount, BigDecimal transferAmount, Long targetId, String name);
}
