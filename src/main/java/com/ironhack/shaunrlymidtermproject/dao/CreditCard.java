package com.ironhack.shaunrlymidtermproject.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.shaunrlymidtermproject.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public class CreditCard extends Account {


    private BigDecimal creditLimit = new BigDecimal("100");
    private BigDecimal interestRate = new BigDecimal("1.2");
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_last_interest_payment")
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
        if (creditLimit.compareTo(new BigDecimal("100000")) == 1) {
            this.creditLimit = new BigDecimal("100000");
        } else {
            this.creditLimit = creditLimit;
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(new BigDecimal("1.1")) == -1) {
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

    public void dateCheck() {
        if (LocalDate.now().isAfter(getDateOfLastInterestPayment().plusMonths(1))) {
            setDateOfLastInterestPayment(LocalDate.now());
            accrueInterest();
        }
    }

    public String accrueInterest() {
        Money newBalance = new Money(getBalance().getAmount().multiply(interestRate));
        BigDecimal interestPayment = newBalance.getAmount().subtract(this.getBalance().getAmount());
        this.setBalance(newBalance);
        return "Accrued interest of " + interestPayment + " on outstanding Balance. Balance is now " + newBalance.getAmount();
    }

    @Override
    public void paymentIn(BigDecimal amount) {
        creditPayment(amount);
    }

    @Override
    public void paymentOut(BigDecimal amount) {
        creditCardPurchase(amount);
    }

    public String creditPayment(BigDecimal amount) {
        String fraudMessage = fraudDetection(amount);
        if (getStatus() != Status.FROZEN) {
            getBalance().decreaseAmount(amount);
            fraudDetection(amount);
            dateCheck();
            return "Payment of " + amount + " received. Current Balance is " + getBalance().toString();
        }
        return fraudMessage;
    }

    public String creditCardPurchase(BigDecimal amount) {
        String fraudMessage = fraudDetection(amount);
        if (getStatus() != Status.FROZEN) {
            if (getBalance().getAmount().add(amount).compareTo(creditLimit) != 1) {
                getBalance().increaseAmount(amount);
                fraudDetection(amount);
                dateCheck();
                return creditLimit.subtract(getBalance().getAmount()) + "credit left on card.";
            } else {
                return "Credit Limit of " + creditLimit + " reached. Payment declined";
            }
        }
        return fraudMessage;
    }
}
