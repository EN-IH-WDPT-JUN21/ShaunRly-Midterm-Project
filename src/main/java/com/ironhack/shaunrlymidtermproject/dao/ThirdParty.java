package com.ironhack.shaunrlymidtermproject.dao;

import com.ironhack.shaunrlymidtermproject.utils.Hasher;
import com.ironhack.shaunrlymidtermproject.utils.MonetaryAmountConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThirdParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashedKey;
    private String name;
    @Convert(converter = MonetaryAmountConverter.class)
    private Money balance;

    public ThirdParty(String key, String name, Money balance) {
        setHashedKey(key);
        this.name = name;
        this.balance = balance;
    }

    public void setHashedKey(String key) {
        this.hashedKey = Hasher.hashGen(key);
    }

    public void paymentIn(BigDecimal amount) {
        getBalance().increaseAmount(amount);
    }

    public void paymentOut(BigDecimal amount){
        getBalance().decreaseAmount(amount);
    }
}
