package com.ironhack.shaunrlymidtermproject.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class TransferDTO {

    private BigDecimal transferAmount;
    private Long targetId;
    private String targetName;
}
