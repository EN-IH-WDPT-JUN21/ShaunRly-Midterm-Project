package com.ironhack.shaunrlymidtermproject.utils;

import com.ironhack.shaunrlymidtermproject.dao.Address;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AddressConverter implements AttributeConverter<Address, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(Address address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.getResidenceNumber());
        sb.append(SEPARATOR);
        sb.append(address.getStreetName());
        sb.append(SEPARATOR);
        sb.append(address.getArea());
        sb.append(SEPARATOR);
        sb.append(address.getCounty());
        sb.append(SEPARATOR);
        sb.append(address.getCountry());
        sb.append(SEPARATOR);
        sb.append(address.getPostcode());
        sb.append(SEPARATOR);
        return sb.toString();
    }

    @Override
    public Address convertToEntityAttribute(String s) {
        String[] pieces = s.split(SEPARATOR);
        Address address = new Address(Integer.parseInt(pieces[0]), pieces[1], pieces[2], pieces[3], pieces[4], pieces[5]);
        System.out.println(address.toString());
        return address;
    }
}
