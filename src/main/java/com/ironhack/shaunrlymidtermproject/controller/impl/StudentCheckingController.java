package com.ironhack.shaunrlymidtermproject.controller.impl;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.controller.interfaces.IStudentCheckingController;
import com.ironhack.shaunrlymidtermproject.dao.StudentChecking;
import com.ironhack.shaunrlymidtermproject.repository.StudentCheckingRepository;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IAccountService;
import com.ironhack.shaunrlymidtermproject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class StudentCheckingController implements IStudentCheckingController {

    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    @Autowired
    IStudentCheckingService studentCheckingService;

    @Autowired
    IAccountService accountService;

    @PostMapping("/student/new")
    @ResponseStatus(HttpStatus.OK)
    public void newStudentChecking(@RequestBody @Valid StudentChecking studentChecking) {
        studentCheckingRepository.save(studentChecking);
    }

    @PutMapping("/student/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable(name = "id") Long id, @RequestBody @Valid StudentChecking studentChecking) {
        studentCheckingService.update(id, studentChecking);
    }

    @GetMapping("/student/admin/getall")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> getAllStudentCheckingAccounts() {
        return studentCheckingRepository.findAll();
    }

    @GetMapping("/student/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking getById(@PathVariable(name = "id") Long id, Principal principal) {
        if (studentCheckingRepository.findById(id).get().getPrimaryOwner().getUsername().equals(principal.getName())) {
            return studentCheckingRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    @DeleteMapping("/student/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable(name = "id") Long id) {
        studentCheckingRepository.deleteById(id);
    }

    @PatchMapping("/student/account/transfer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentChecking transfer(@PathVariable(name = "id") Long id,
                                    @RequestBody @Valid TransferDTO transferDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (studentCheckingRepository.findById(id).get().getPrimaryOwner().getUsername().equals(userDetails.getUsername())) {
            StudentChecking updatedTarget = (StudentChecking) accountService.moneyTransfer(studentCheckingRepository.findById(id).get(),
                    transferDTO.getTransferAmount(), transferDTO.getTargetId(), transferDTO.getTargetName());
            studentCheckingRepository.save(updatedTarget);
            return updatedTarget;
        } else {
            return null;
        }
    }

}
