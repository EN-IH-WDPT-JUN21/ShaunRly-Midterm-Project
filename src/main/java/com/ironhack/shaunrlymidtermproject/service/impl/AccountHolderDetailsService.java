package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.AccountHolder;
import com.ironhack.shaunrlymidtermproject.dao.User;
import com.ironhack.shaunrlymidtermproject.repository.AccountHolderRepository;
import com.ironhack.shaunrlymidtermproject.repository.UserRepository;
import com.ironhack.shaunrlymidtermproject.security.AccountHolderDetails;
//import com.ironhack.shaunrlymidtermproject.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountHolderDetailsService implements UserDetailsService {

    @Autowired
    AccountHolderRepository accountHolderRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountHolder> accountHolder = accountHolderRepository.findByUsername(username);

        if(!accountHolder.isPresent()){
            throw new UsernameNotFoundException("User not found with username " + username);
        }

        AccountHolderDetails AHD = new AccountHolderDetails(accountHolder.get());
        return AHD;
    }
}
