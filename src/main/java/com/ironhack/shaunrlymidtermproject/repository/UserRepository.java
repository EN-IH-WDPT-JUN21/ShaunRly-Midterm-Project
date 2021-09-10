package com.ironhack.shaunrlymidtermproject.repository;

import com.ironhack.shaunrlymidtermproject.dao.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AccountHolder, Long> {
    Optional<AccountHolder> findByUsername(String name);
}
