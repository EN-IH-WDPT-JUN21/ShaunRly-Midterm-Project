package com.ironhack.shaunrlymidtermproject.repository;

import com.ironhack.shaunrlymidtermproject.dao.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
}
