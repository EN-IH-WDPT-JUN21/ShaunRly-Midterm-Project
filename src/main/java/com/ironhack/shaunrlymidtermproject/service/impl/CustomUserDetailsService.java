//package com.ironhack.shaunrlymidtermproject.service.impl;
//
//import com.ironhack.shaunrlymidtermproject.dao.User;
//import com.ironhack.shaunrlymidtermproject.repository.UserRepository;
////import com.ironhack.shaunrlymidtermproject.security.CustomUserDetails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUsername(username);
//
//        if(!user.isPresent()){
//            throw new UsernameNotFoundException("User not found with username " + username);
//        }
//
//        CustomUserDetails cud = new CustomUserDetails(user.get());
//        return cud;
//    }
//}
