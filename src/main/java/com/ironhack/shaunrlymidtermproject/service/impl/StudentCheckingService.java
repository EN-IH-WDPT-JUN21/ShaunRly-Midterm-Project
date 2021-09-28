package com.ironhack.shaunrlymidtermproject.service.impl;

import com.ironhack.shaunrlymidtermproject.dao.StudentChecking;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentCheckingService implements IStudentCheckingService {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    IAccountService accountService;

    public void update(Long id, StudentChecking studentChecking) {

        Optional<StudentChecking> storedStudentChecking = studentCheckingRepository.findById(id);
        if (storedStudentChecking.isPresent()) {
            StudentChecking storedSuperUpdated = (StudentChecking) accountService.updateSuper(id, List.of(storedStudentChecking.get(), studentChecking));
            studentCheckingRepository.save(storedSuperUpdated);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tha Student Checking Account does not exists");
        }

    }
}
