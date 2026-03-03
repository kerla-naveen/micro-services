package com.DigiClassRoom.OrderService.config;

import com.DigiClassRoom.OrderService.external.decoder.CustomDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder errorDecoder(){
        return new CustomDecoder();
    }
}
