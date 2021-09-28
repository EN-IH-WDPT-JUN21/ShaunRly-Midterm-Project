package com.ironhack.shaunrlymidtermproject.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Hasher {

    public static PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();

    public static String hashGen(String key) {
        return pwdEnconder.encode(key);
    }
}
