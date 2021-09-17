package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.interfaces.IStudentCheckingController;
import com.ironhack.shaunrlymidtermproject.dao.CreditCard;
import com.ironhack.shaunrlymidtermproject.dao.StudentChecking;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class StudentCheckingController implements IStudentCheckingController {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    IStudentCheckingService studentCheckingService;

    @PutMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid StudentChecking studentChecking){
        studentCheckingService.update(id, studentChecking);
    }

}
