package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.utils.AddressConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private LocalDate dateOfBirth;
    @Convert(converter = AddressConverter.class)
    private Address primaryAddress;
    @Convert(converter = AddressConverter.class)
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Account> accounts;

    public AccountHolder(String username, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        }

    public AccountHolder(String username, LocalDate dateOfBirth, Address primaryAddress) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = primaryAddress;
    }
}


