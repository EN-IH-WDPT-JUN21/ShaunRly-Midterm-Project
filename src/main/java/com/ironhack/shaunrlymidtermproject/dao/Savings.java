package com.ironhack.shaunrlymidtermproject.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.shaunrlymidtermproject.enums.Status;
import com.ironhack.shaunrlymidtermproject.utils.MonetaryAmountConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public class Savings extends Account{


    @Column(name = "savings_interest_rate", columnDefinition = "DECIMAL(5,4)")
    @Positive
    private BigDecimal interestRate = new BigDecimal("1.0025");
    @Convert(converter = MonetaryAmountConverter.class)
    private Money minimumBalance = new Money(new BigDecimal("1000"));
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_last_interest_payment")
    private LocalDate dateOfLastInterestPayment = LocalDate.now();

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
        this.interestRate = interestRate.max(new BigDecimal("1.5"));
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = new Money(minimumBalance.getAmount().min(new BigDecimal("100")));
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
        Money newBalance = new Money(getBalance().getAmount().multiply(interestRate));
        BigDecimal interestPayment = newBalance.getAmount().subtract(this.getBalance().getAmount());
        this.setBalance(newBalance);
        return "Received Interest payment of " + interestPayment + ". Balance is now " + newBalance.getAmount();
    }

    @Override
    public void paymentOut(BigDecimal amount){
        fraudDetection(amount);
        if (getStatus() != Status.FROZEN) {
            getBalance().decreaseAmount(amount);
            if (getBalance().getAmount().compareTo(minimumBalance.getAmount()) == -1) {
                getBalance().decreaseAmount(getPenaltyFee());
            }
            fraudDetection(amount);
            dateCheck();
        }
    }

    @Override
    public void paymentIn(BigDecimal amount){
        fraudDetection(amount);
        if (getStatus() != Status.FROZEN) {
            getBalance().increaseAmount(amount);
            fraudDetection(amount);
            dateCheck();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "Savings{" +
                "interestRate=" + interestRate +
                ", minimumBalance=" + minimumBalance +
                ", dateOfLastInterestPayment=" + dateOfLastInterestPayment +
                '}';
    }
}
