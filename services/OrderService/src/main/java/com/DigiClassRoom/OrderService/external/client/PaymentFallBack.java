package com.DigiClassRoom.OrderService.external.client;

import com.DigiClassRoom.OrderService.exception.CustomException;
import com.DigiClassRoom.OrderService.external.request.PaymentRequest;
import com.DigiClassRoom.OrderService.external.response.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentFallBack  implements PaymentService{
    @Override
    public ResponseEntity<Long> doPayment(PaymentRequest paymentRequest) {
        throw new CustomException("service temporarily down","PAYMENT_SERVICE_DOWN",503);
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
       throw new CustomException("payment service temporarily down","PAYMENT_SERVICE_DOWN",503);
    }
}
