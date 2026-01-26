package com.DigiClassRoom.OrderService.exception;

import com.DigiClassRoom.OrderService.external.response.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseEntity<ErrorResponse> customExceptionHandler(CustomException customException){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(customException.getMessage())
                        .errorCode(customException.getErrorCode())
                .build(),
                HttpStatusCode.valueOf(customException.getStatus())
        );
    }
}
