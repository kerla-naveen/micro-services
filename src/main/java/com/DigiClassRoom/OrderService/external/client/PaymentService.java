package com.DigiClassRoom.OrderService.external.client;

import com.DigiClassRoom.OrderService.exception.CustomException;
import com.DigiClassRoom.OrderService.external.request.PaymentRequest;
import com.DigiClassRoom.OrderService.external.response.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", path = "/payment", fallback = PaymentFallBack.class)
public interface PaymentService {

    @PostMapping
    ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    @GetMapping
    PaymentResponse getPaymentDetailsByOrderId(@PathVariable long orderId);

}
