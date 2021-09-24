package com.ironhack.shaunrlymidtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CreditCard extends Account{



    private BigDecimal creditLimit = new BigDecimal("100");
    private BigDecimal interestRate = new BigDecimal("1.2");
    private LocalDate dateOfLastInterestPayment;

    public CreditCard(Money balance,
                      AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance, BigDecimal creditLimit,
                      AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance,
                      AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public CreditCard(Money balance, BigDecimal creditLimit,
                      AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
        setDateOfLastInterestPayment(getCreationDate());
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        if (creditLimit.compareTo(new BigDecimal("100000")) == 1){
            this.creditLimit = new BigDecimal("100000");
        } else {
            this.creditLimit = creditLimit;
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(new BigDecimal("1.1")) == -1){
            this.interestRate = new BigDecimal("1.1");
        } else {
            this.interestRate = interestRate;
        }
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
        Money newBalance = new Money(getBalance().getAmount().multiply(interestRate));
        BigDecimal interestPayment = newBalance.getAmount().subtract(this.getBalance().getAmount());
        this.setBalance(newBalance);
        return "Accrued interest of " + interestPayment + " on outstanding Balance. Balance is now " + newBalance.getAmount();
    }

    public String creditPayment(BigDecimal amount){
        getBalance().decreaseAmount(amount);
        dateCheck();
        return "Payment of " + amount + " received. Current Balance is " + getBalance().toString();
    }

    public String creditCardPurchase(BigDecimal amount){
        if (getBalance().getAmount().add(amount).compareTo(creditLimit) != 1) {
            getBalance().increaseAmount(amount);
            dateCheck();
            return creditLimit.subtract(getBalance().getAmount()) + "credit left on card.";
        } else {
            return "Credit Limit of " + creditLimit + " reached. Payment declined";
        }

    }
}
