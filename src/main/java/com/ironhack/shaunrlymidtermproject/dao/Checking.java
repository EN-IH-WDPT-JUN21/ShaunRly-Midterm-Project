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

    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    private Money minimumBalance = new Money(new BigDecimal("250"));

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }

    @Override
    public void paymentOut(BigDecimal amount){
        getBalance().decreaseAmount(amount);
        if(getBalance().getAmount().compareTo(minimumBalance.getAmount()) == -1){
            getBalance().decreaseAmount(getPenaltyFee());
        }
    }
}
