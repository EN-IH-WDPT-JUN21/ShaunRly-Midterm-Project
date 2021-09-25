package com.ironhack.shaunrlymidtermproject.service.interfaces;

import com.ironhack.shaunrlymidtermproject.dao.ThirdParty;

import java.math.BigDecimal;

public interface IThirdPartyService {
    public void update(Long id, ThirdParty thirdParty) throws Exception;

    String moneyTransfer(ThirdParty thirdParty, String hashedKey, BigDecimal transferAmount, Long targetId, String secretKey);
}
