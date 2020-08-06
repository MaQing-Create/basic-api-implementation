package com.thoughtworks.rslist.tools;

import com.thoughtworks.rslist.exception.CommonException;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidIndexException.class)
    ResponseEntity exceptionHandler(InvalidIndexException ex){
        Logger logger = LoggerFactory.getLogger(LoggingController.class);
        logger.error("这是由于index越界造成的错误");
        CommonException commonException = new CommonException();
        commonException.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException);
    }
}
