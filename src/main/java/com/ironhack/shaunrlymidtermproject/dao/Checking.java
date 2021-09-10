package com.ironhack.shaunrlymidtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Checking extends Account{

    private BigDecimal monthlyMaintenanceFee;
    private Money minimumBalance;

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal monthlyMaintenanceFee, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setMonthlyMaintenanceFee(monthlyMaintenanceFee);
        setMinimumBalance(minimumBalance);
    }
}
