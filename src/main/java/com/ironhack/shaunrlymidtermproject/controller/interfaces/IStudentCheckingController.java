package com.ironhack.shaunrlymidtermproject.controller.interfaces;

import com.ironhack.shaunrlymidtermproject.controller.DTO.TransferDTO;
import com.ironhack.shaunrlymidtermproject.dao.StudentChecking;

import java.util.List;

public interface IStudentCheckingController {
    public void newStudentChecking(StudentChecking studentChecking);
    public List<StudentChecking> getAllStudentCheckingAccounts();
    public void update(Long id, StudentChecking studentChecking);
    public void deleteById(Long id);
    public StudentChecking transfer(Long id, TransferDTO transferDTO);
}
