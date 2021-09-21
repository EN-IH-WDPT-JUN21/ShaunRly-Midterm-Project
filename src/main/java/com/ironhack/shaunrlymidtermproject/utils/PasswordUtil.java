package com.ironhack.shaunrlymidtermproject.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    public static void main(String[] args) {
        System.out.println(encryptedPassword("Password"));
    }

    public static String encryptedPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        return passwordEncoder.encode(password);
    }
}
