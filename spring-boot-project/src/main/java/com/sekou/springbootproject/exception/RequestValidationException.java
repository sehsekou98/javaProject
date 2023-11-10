package com.sekou.springbootproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class RequestValidationException extends RuntimeException{
    public RequestValidationException(String message) {
        super(message);
    }
}
