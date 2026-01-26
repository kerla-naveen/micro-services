package com.DigiClassRoom.OrderService.external.decoder;

import com.DigiClassRoom.OrderService.exception.CustomException;
import com.DigiClassRoom.OrderService.external.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper= new ObjectMapper();
        try {
            ErrorResponse errorResponse=
                    objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

            return new CustomException(
                    errorResponse.getMessage(),
                    errorResponse.getErrorCode(),
                    response.status()
            );

        } catch (IOException e) {
            return new CustomException(
                    "internal server error",
                    "INTERNAL_SERVER_ERROR",
                    500
            );
        }
    }
}
