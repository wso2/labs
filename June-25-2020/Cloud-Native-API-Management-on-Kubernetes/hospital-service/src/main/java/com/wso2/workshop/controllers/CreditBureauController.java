/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2.workshop.controllers;

import com.wso2.workshop.dao.BureauResponse;
import com.wso2.workshop.dao.CreditApprovalRequest;
import com.wso2.workshop.dao.CreditPaper;
import com.wso2.workshop.dao.CreditProposal;
import com.wso2.workshop.util.InternalOperations;
import com.wso2.workshop.util.MalformedRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 * @author ck
 */
@RestController
public class CreditBureauController {
    private final InternalOperations util = new InternalOperations();

    @PostMapping("/bureau/credit/proposals")
    public BureauResponse createProposal(@RequestBody CreditProposal creditProposal) {
        if (util.isValidCreditProposal(creditProposal)) {
            int creditScore = util.getCreditScore();
            double requestedAmount = creditProposal.getRequestedLoanAmount();
            double approvalAmount = util.getApprovableAmount(creditScore,requestedAmount,creditProposal.getYearsLivedInUK());
            BureauResponse bureauResponse = new BureauResponse(creditProposal.getId(),util.getDate(),creditScore,requestedAmount,approvalAmount,"Pending");
            return bureauResponse;
        }else{
            throw new MalformedRequestException();
        }
    }
}
