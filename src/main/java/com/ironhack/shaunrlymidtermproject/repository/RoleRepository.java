package com.ironhack.shaunrlymidtermproject.repository;

import com.ironhack.shaunrlymidtermproject.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
