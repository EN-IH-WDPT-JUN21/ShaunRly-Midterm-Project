package com.ironhack.shaunrlymidtermproject.service.interfaces;

import com.ironhack.shaunrlymidtermproject.dao.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    Account updateSuper(Long id, List<? extends Account> Accounts);
}
