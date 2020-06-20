/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2.workshop.util;

/**
 *
 * @author ck
 */
public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String id) {
        super("Couldn't find a Customer with ID " + id);
    }
}
