package com.karthik.projects.airBnbApp.exceptions;

public class UnAuthorisedException extends RuntimeException{
    public UnAuthorisedException(String message){
        super(message);
    }
}
