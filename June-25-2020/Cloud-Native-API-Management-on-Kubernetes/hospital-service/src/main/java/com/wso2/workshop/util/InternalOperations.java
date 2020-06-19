/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2.workshop.util;

import com.wso2.workshop.dao.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author ck
 */
public class InternalOperations {

    public HashMap<String, Customer> getCustomerMap(){
        HashMap<String, Customer> customersMap = new HashMap<>();

        Contact contact = new Contact("+447720821292","Sample Address 1","jessey@gmail.com");
        Customer customer = new Customer("Jessey","Pinkman","301083909",contact);
        customersMap.put(customer.getId(),customer);

        contact = new Contact("+447890128663","Sample Address 2","walter@gmail.com");
        customer = new Customer("Walter","White","301091723",contact);
        customersMap.put(customer.getId(),customer);

        contact = new Contact("+447828287887","Sample Address 4","mike@gmail.com");
        customer = new Customer("Michael","Ehmentraut","301082344",contact);
        customersMap.put(customer.getId(),customer);

        contact = new Contact("+447823348282","Sample Address 4","lalo@gmail.com");
        customer = new Customer("Eduardo","Salamanca","301088231",contact);
        customersMap.put(customer.getId(),customer);

        return customersMap;
    }

    public String createReservationID(String customerName) {
        String reservationID = Base64.getEncoder().encodeToString(customerName.getBytes());
        return reservationID.toUpperCase();
    }

    public int getCreditScore(){
        Random random = new Random();
        return random.nextInt(300) + 600;
    }

    public String getDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

    public boolean isValidCreditApprovalRequest(CreditApprovalRequest r){
        return !(isInvalid(r.getCustomerId()) || isInvalid(r.getCreditType()) || isInvalid(r.getRequestedAmount()));
    }

    private boolean isInvalid(String parameter){
        return parameter == null || parameter.isEmpty();
    }
    private boolean isInvalid(int parameter){
        return parameter <= 0;
    }
    private boolean isInvalid(CreditType parameter){
        return !(parameter == CreditType.OVERDRAUGHT || parameter == CreditType.TERM_LOAN);
    }
    private boolean isInvalid(double parameter){
        return parameter <= 0;
    }

    public double getApprovableAmount(int creditScore, double requestedAmount,int yearsInTheUK) {
        if(yearsInTheUK <= 2){
            return 0;
        }
        if (creditScore > 800){
            return requestedAmount;
        }else if(creditScore > 700){
            return requestedAmount * 0.8;
        }else if(creditScore>600){
            return requestedAmount * 0.6;
        }else{
            return requestedAmount * 0.35;
        }
    }

    public boolean isValidCreditProposal(CreditProposal p) {
        return !(isInvalid(p.getFirstName())
        || isInvalid(p.getLastName())
        || isInvalid(p.getAddress())
        || isInvalid(p.getRequestedLoanAmount())
        || isInvalid(p.getYearsLivedInUK())
        || isInvalid(p.getId()));
    }

    public boolean isValidCreditOverdraughtRequest(OverdraughtRequest o) {
        return !(isInvalid(o.getCreditApplicationReference())
        || isInvalid(o.getCustomerFisrtName())
        || isInvalid(o.getCustomerLastName())
        || isInvalid(o.getCustomerId())
        || isInvalid(o.getPhoneNumber())
        || isInvalid(o.getRequestedAmount()));
    }

    public double getOutstanding(double requestedAmount) {
        Random random = new Random();
        return requestedAmount * 0.1 * random.nextInt(3);
    }

    public double getApprovableAmount(double requestedAmount, double outstanding) {
        if(requestedAmount < 1500){
            return requestedAmount;
        }else if(requestedAmount < 8000){
            return (requestedAmount - outstanding);
        }else{
            return (8000.00 - outstanding);
        }
    }
}
