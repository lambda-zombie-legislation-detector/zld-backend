package com.legicycle.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class InvalidInputException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message)
    {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause)
    {
        super(message, cause);
    }
}