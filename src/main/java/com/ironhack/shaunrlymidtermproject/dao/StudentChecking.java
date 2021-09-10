package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentChecking extends Account{

    public StudentChecking(Long id, Money balance, String secretKey, AccountHolder primaryOwner,
                           AccountHolder secondaryOwner, Money penaltyFee, LocalDate creationDate, Status status) {
        super(balance, primaryOwner, secondaryOwner);
    }
}
