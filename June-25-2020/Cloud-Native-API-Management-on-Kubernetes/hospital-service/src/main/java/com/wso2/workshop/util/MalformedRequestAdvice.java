/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wso2.workshop.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author ck
 */
@ControllerAdvice
public class MalformedRequestAdvice {
    @ResponseBody
    @ExceptionHandler(MalformedRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeNotFoundHandler(MalformedRequestException ex) {
        return ex.getMessage();
    }
}
