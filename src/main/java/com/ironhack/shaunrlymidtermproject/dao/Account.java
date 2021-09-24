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
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = MonetaryAmountConverter.class)
    private Money balance;

    private String secretKey = createSecretKey();
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AccountHolder primaryOwner;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AccountHolder secondaryOwner;
    private BigDecimal penaltyFee = new BigDecimal("40");
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate creationDate = LocalDate.now();
    private Status status = Status.ACTIVE;

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        if(secondaryOwner == null){
            this.secondaryOwner = primaryOwner;
        } else {
            this.secondaryOwner = secondaryOwner;
        }
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    private String createSecretKey(){
        return "Placeholder";
    }

    public void paymentIn(BigDecimal amount){
        balance.increaseAmount(amount);
    }

    public void paymentOut(BigDecimal amount){
        balance.decreaseAmount(amount);
    }

    public static Account createChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner){
        if (Period.between(primaryOwner.getDateOfBirth(), LocalDate.now()).getYears() >= 24){
            return new Checking(balance, primaryOwner, secondaryOwner);
        } else {
            return new StudentChecking(balance, primaryOwner, secondaryOwner);
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", secretKey='" + secretKey + '\'' +
                ", primaryOwner=" + primaryOwner +
                ", secondaryOwner=" + secondaryOwner +
                ", penaltyFee=" + penaltyFee +
                ", creationDate=" + creationDate +
                ", status=" + status +
                '}';
    }
}
