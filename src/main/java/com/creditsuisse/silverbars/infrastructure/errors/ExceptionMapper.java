package com.creditsuisse.silverbars.infrastructure.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.security.InvalidParameterException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@ControllerAdvice(basePackages = "com.creditsuisse.infrastructure.controllers")
@RequestMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class ExceptionMapper {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> catchAll(
            HttpServletRequest request,
            Exception e) {

        log.error("Unknown exception", e);

        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<String> handleInvalidParamerterException(
            HttpServletRequest request,
            Exception e) {

        log.error("InvalidParameterException:", e);

        return new ResponseEntity<>(BAD_REQUEST);
    }
}