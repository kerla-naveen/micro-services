package com.DigiClassRoom.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    String orderServiceFallBack(){
        return "Order Service is currently down...!";
    }
}
