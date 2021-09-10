package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Money balance;
    private final String secretKey = createSecretKey();
    private AccountHolder primaryOwner;
    private AccountHolder secondaryOwner;
    private BigDecimal penaltyFee = BigDecimal.valueOf(40);
    private LocalDate creationDate = LocalDate.now();
    private Status status = Status.ACTIVE;

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
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

}
