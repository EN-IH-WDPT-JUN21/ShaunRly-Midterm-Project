package com.ironhack.shaunrlymidtermproject.dao;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = MonetaryAmountConverter.class)
    private Money balance;

    private final String secretKey = createSecretKey();
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountHolder primaryOwner;
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountHolder secondaryOwner;
    private BigDecimal penaltyFee = BigDecimal.valueOf(40);
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
