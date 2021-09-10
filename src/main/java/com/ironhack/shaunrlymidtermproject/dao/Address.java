package com.ironhack.shaunrlymidtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

    private int residenceNumber;
    private String streetName;
    private String area;
    private String county;
    private String country;
    private String postcode;
}
