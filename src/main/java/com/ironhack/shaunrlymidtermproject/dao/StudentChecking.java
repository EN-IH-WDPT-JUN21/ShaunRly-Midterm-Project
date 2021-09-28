package com.ironhack.shaunrlymidtermproject.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
public class StudentChecking extends Account {


    private boolean canUpgrade = false;

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }

    public String canUpgrade() {
        if (this.getPrimaryOwner().getDateOfBirth().plusYears(24).isBefore(LocalDate.now())) {
            setCanUpgrade(true);
        }
        return "StudentChecking{" +
                "canUpgrade=" + canUpgrade +
                '}';
    }

}
