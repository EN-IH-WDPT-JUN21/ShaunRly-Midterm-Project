package com.ironhack.shaunrlymidtermproject.utils;

import com.ironhack.shaunrlymidtermproject.dao.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.util.Currency;


@Converter
public class MonetaryAmountConverter implements AttributeConverter<Money, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(Money money) {
        StringBuilder sb = new StringBuilder();
        sb.append(money.getCurrency().toString());
        sb.append(SEPARATOR);
        sb.append(money.getAmount().toString());
        return sb.toString();
    }

    @Override
    public Money convertToEntityAttribute(String s) {
        String[] pieces = s.split(SEPARATOR);
        Money money = new Money(BigDecimal.valueOf(Double.parseDouble(pieces[1])), Currency.getInstance(pieces[0]));
        return money;
    }
}
