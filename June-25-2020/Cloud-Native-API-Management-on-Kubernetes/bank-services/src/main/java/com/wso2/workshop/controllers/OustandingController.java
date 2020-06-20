package com.wso2.workshop.controllers;

import com.wso2.workshop.dao.BureauResponse;
import com.wso2.workshop.dao.CreditProposal;
import com.wso2.workshop.dao.OverdraughtRequest;
import com.wso2.workshop.dao.OverdraughtResponse;
import com.wso2.workshop.util.InternalOperations;
import com.wso2.workshop.util.MalformedRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OustandingController {
    private final InternalOperations util = new InternalOperations();

    @PostMapping("/accounts-credit-service/overdraught")
    public OverdraughtResponse requestOverdraught(@RequestBody OverdraughtRequest overdraughtRequest) {
        if (util.isValidCreditOverdraughtRequest(overdraughtRequest)) {
            double requestedAmount = overdraughtRequest.getRequestedAmount();
            double outstanding = util.getOutstanding(requestedAmount);
            double approvalAmount = util.getApprovableAmount(requestedAmount,outstanding);
            OverdraughtResponse overdraughtResponse = new OverdraughtResponse(overdraughtRequest.getCreditApplicationReference(),"Approved",overdraughtRequest.getRequestedAmount(),outstanding,approvalAmount);
            return overdraughtResponse;
        }else{
            throw new MalformedRequestException();
        }
    }
}
