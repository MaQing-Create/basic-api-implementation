package com.thoughtworks.rslist.tools;

import com.thoughtworks.rslist.exception.CommonException;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidIndexException.class)
    ResponseEntity exceptionHandler(InvalidIndexException ex){
        CommonException commonException = new CommonException();
        commonException.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
    }
}
