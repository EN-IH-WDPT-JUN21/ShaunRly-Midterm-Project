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
public class CreditCard extends Account{

    private Money creditLimit = new Money(BigDecimal.valueOf(100));
    private BigDecimal interestRate = BigDecimal.valueOf(0.2);
    private LocalDate dateOfLastInterestPayment;

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                      Money creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                      BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner,
                      Money creditLimit, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public void setCreditLimit(Money creditLimit) {
        this.creditLimit = new Money(creditLimit.getAmount().max(BigDecimal.valueOf(100000)));
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate.min(BigDecimal.valueOf(0.1));
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
        if (LocalDate.now().isAfter(getDateOfLastInterestPayment().plusMonths(1))){
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
}
