/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2.workshop.controllers;

import com.wso2.workshop.dao.*;
import com.wso2.workshop.dao.CreditPaper;
import com.wso2.workshop.util.InternalOperations;
import com.wso2.workshop.util.MalformedRequestException;
import com.wso2.workshop.util.CustomerNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditApplicationController {

    private final InternalOperations util = new InternalOperations();

    private final Map<String,CreditPaper> creditPapers = new HashMap<>();
    private Map<String, Customer> customers = null;

    public CreditApplicationController() {
        customers = util.getCustomerMap();
    }

    @GetMapping("/credit-service/applications")
    Map<String,CreditPaper> getCreditPapers() {
        return creditPapers;
    }

    @GetMapping("/credit-service/applications/{reference}")
    CreditPaper getCreditPaper(@PathVariable String reference) {
        CreditPaper creditPaper = creditPapers.get(reference);
        if (creditPaper == null) {
            throw new CustomerNotFoundException(reference);
        } else {
            return creditPaper;
        }
    }

    @PostMapping("/credit-service/applications")
    public CreditPaper createCreditApplication(@RequestBody CreditApprovalRequest approvalRequest) {
        if (util.isValidCreditApprovalRequest(approvalRequest)) {
            CreditPaper paper = new CreditPaper(approvalRequest,customers.get(approvalRequest.getCustomerId()));
            creditPapers.put(paper.getReference(), paper);
            return paper;
        }else{
            throw new MalformedRequestException();
        }
    }

    @GetMapping("/core-banking/customers")
    Map<String,Customer> getCustomers() {
        return customers;
    }

    @GetMapping("/core-banking/customers/{id}")
    Customer getCustomer(@PathVariable String id) {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        } else {
            return customer;
        }
    }
}
