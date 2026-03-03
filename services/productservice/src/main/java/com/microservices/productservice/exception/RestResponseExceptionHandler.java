package com.microservices.productservice.exception;

import com.microservices.productservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    ResponseEntity<ErrorResponse> handleProductServiceExceptionHandler(ProductServiceCustomException productServiceCustomException){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(productServiceCustomException.getMessage())
                .errorCode(productServiceCustomException.getErrorCode())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
}
