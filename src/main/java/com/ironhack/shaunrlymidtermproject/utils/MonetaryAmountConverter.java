package com.ironhack.shaunrlymidtermproject.utils;

import com.ironhack.shaunrlymidtermproject.dao.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;
import java.util.Currency;


@Converter
public class MonetaryAmountConverter implements AttributeConverter<Money, String> {

    private static final String SEPERATOR = "|";

    @Override
    public String convertToDatabaseColumn(Money money) {
        StringBuilder sb = new StringBuilder();
        sb.append(money.getCurrency().toString());
        sb.append(SEPERATOR);
        sb.append(money.getAmount().toString());
        return sb.toString();
    }

    @Override
    public Money convertToEntityAttribute(String s) {
        String[] pieces = s.split(SEPERATOR);
        Money money = new Money(BigDecimal.valueOf(Long.parseLong(pieces[0])), Currency.getInstance(pieces[1]));
        return money;
    }
}
