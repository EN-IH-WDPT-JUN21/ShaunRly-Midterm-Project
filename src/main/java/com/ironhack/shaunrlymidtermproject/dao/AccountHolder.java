package com.ironhack.shaunrlymidtermproject.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.shaunrlymidtermproject.utils.AddressConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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

    private String name;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate dateOfBirth;
    @Convert(converter = AddressConverter.class)
    private Address primaryAddress;
    @Convert(converter = AddressConverter.class)
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonBackReference
    private Set<Account> accounts;

    private String username;

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = primaryAddress;
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress, String username) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        this.username = username;
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, String username) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = primaryAddress;
        this.username = username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}


