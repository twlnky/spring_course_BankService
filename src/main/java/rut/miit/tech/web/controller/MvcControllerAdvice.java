package rut.miit.tech.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcControllerAdvice {

    @ExceptionHandler
    public String handleException(Exception exception){
        exception.printStackTrace();
        return "domain/InternalServerError";
    }



}
