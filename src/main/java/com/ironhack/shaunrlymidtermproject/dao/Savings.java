package com.ironhack.shaunrlymidtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Savings extends Account{

    private BigDecimal interestRate = BigDecimal.valueOf(0.0025);
    private Money minimumBalance = new Money(BigDecimal.valueOf(1000));
    private LocalDate dateOfLastInterestPayment;

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setMinimumBalance(minimumBalance);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                   BigDecimal interestRate, Money minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate.max(BigDecimal.valueOf(0.5));
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = new Money(minimumBalance.getAmount().min(BigDecimal.valueOf(100)));
    }

    @Override
    public void setBalance(Money balance) {
        dateCheck();
        super.setBalance(balance);
    }

    @Override
    public Money getBalance() {
        dateCheck();
        return super.getBalance();
    }

    public void dateCheck(){
        if (LocalDate.now().isAfter(getDateOfLastInterestPayment().plusYears(1))){
            setDateOfLastInterestPayment(LocalDate.now());
            accrueInterest();
        }
    }

    public String accrueInterest(){
        Money newBalance = new Money(getBalance().getAmount().multiply(BigDecimal.valueOf(1).add(interestRate)));
        BigDecimal interestPayment = newBalance.getAmount().subtract(this.getBalance().getAmount());
        this.setBalance(newBalance);
        return "Received Interest payment of " + interestPayment + ". Balance is now " + newBalance.getAmount();
    }

    @Override
    public void paymentOut(BigDecimal amount){
        getBalance().decreaseAmount(amount);
        if(getBalance().getAmount().compareTo(minimumBalance.getAmount()) == -1){
            getBalance().decreaseAmount(getPenaltyFee());
        }
    }
}
