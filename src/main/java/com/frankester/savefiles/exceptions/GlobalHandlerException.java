package com.frankester.savefiles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> fileNotFoundException(FileNotFoundException ex){

        Map<String, String> errorMessage = new HashMap<>();

        errorMessage.put("message", ex.getLocalizedMessage());

        return errorMessage;
    }

}
