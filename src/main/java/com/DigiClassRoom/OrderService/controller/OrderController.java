package com.DigiClassRoom.OrderService.controller;

import com.DigiClassRoom.OrderService.model.OrderRequest;
import com.DigiClassRoom.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/placeOrder")
    ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        Long orderId=orderService.placeOrder(orderRequest);
        log.info("orderId:{}",orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
