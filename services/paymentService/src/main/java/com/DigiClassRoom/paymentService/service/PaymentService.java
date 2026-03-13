package com.DigiClassRoom.paymentService.service;

import com.DigiClassRoom.paymentService.model.PaymentRequest;
import com.DigiClassRoom.paymentService.model.PaymentResponse;

public interface PaymentService {
    PaymentResponse getPaymentDetailsByOrderId(long orderId);
    Long doPayment(PaymentRequest paymentRequest);
}
