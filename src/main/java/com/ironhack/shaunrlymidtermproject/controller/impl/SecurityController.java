package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.dao.User;
import com.ironhack.shaunrlymidtermproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/newuser")
    @ResponseStatus(HttpStatus.OK)
    public String newUser(@RequestBody String username,
                          @RequestBody String password) {
        for (User user : userRepository.findAll()) {
            if (user.getUsername().equals(username)) {
                return "That username is not available";
            }
        }
        userRepository.save(new User(username, password));
        return "Successfully created new User with Username: " + username;
    }
}
