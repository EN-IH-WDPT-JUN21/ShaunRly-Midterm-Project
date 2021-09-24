package com.ironhack.shaunrlymidtermproject.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.shaunrlymidtermproject.utils.MonetaryAmountConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Checking extends Account{


    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    @Convert(converter = MonetaryAmountConverter.class)
    private Money minimumBalance = new Money(new BigDecimal("250"));
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate dateOfLastMaintenancePayment = LocalDate.now();

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }

    @Override
    public Money getBalance() {
        dateCheck();
        return super.getBalance();
    }

    @Override
    public void setBalance(Money balance) {
        super.setBalance(balance);
        dateCheck();
    }

    @Override
    public void paymentIn(BigDecimal amount) {
        super.paymentIn(amount);
        dateCheck();
    }

    @Override
    public void paymentOut(BigDecimal amount){
        getBalance().decreaseAmount(amount);
        if(getBalance().getAmount().compareTo(minimumBalance.getAmount()) == -1){
            getBalance().decreaseAmount(getPenaltyFee());
        }
        dateCheck();
    }

    public void dateCheck(){
        if (LocalDate.now().isAfter(getDateOfLastMaintenancePayment().plusMonths(1))){
            setDateOfLastMaintenancePayment(LocalDate.now());
            makeMaintenancePayment();
        }
    }

    public String makeMaintenancePayment(){
        Money newBalance = new Money(getBalance().getAmount().subtract(monthlyMaintenanceFee));
        this.setBalance(newBalance);
        return "Monthly Maintenance Fee of " + getMonthlyMaintenanceFee() +
                "deducted from balance. Balance is now " + newBalance.getAmount();
    }
}
