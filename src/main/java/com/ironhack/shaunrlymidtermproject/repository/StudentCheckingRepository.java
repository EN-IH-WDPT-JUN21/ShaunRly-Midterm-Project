package com.ironhack.shaunrlymidtermproject.repository;

import com.ironhack.shaunrlymidtermproject.dao.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking, Long> {
}
