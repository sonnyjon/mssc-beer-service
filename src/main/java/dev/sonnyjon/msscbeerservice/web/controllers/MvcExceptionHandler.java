package dev.sonnyjon.msscbeerservice.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonny on 8/24/2022.
 */
@ControllerAdvice
public class MvcExceptionHandler
{
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List> validationErrorHandler(ConstraintViolationException cve)
    {
        List<String> errorsList = new ArrayList<>(cve.getConstraintViolations().size());

        cve.getConstraintViolations()
            .forEach(error -> errorsList.add( error.toString() ));

        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

}