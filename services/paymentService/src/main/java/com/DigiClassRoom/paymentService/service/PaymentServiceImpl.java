package com.DigiClassRoom.paymentService.service;

import com.DigiClassRoom.paymentService.entity.TransactionalDetails;
import com.DigiClassRoom.paymentService.model.PaymentMode;
import com.DigiClassRoom.paymentService.model.PaymentRequest;
import com.DigiClassRoom.paymentService.model.PaymentResponse;
import com.DigiClassRoom.paymentService.repository.TransactionalDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    TransactionalDetailsRepository transactionalDetailsRepository;

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {

        log.info("payment is processing:{}",paymentRequest);
        TransactionalDetails transactionalDetails=
                TransactionalDetails.builder()
                        .paymentDate(Instant.now())
                        .paymentMode(paymentRequest.getPaymentMode().name())
                        .ReferenceNumber(paymentRequest.getReferenceNumber())
                        .orderId(paymentRequest.getOrderId())
                        .amount(paymentRequest.getAmount())
                        .paymentStatus("SUCCESS")
                        .build();
        transactionalDetailsRepository.save(transactionalDetails);

        log.info("payment done successfully:{}",transactionalDetails.getId());
        return transactionalDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {

        TransactionalDetails transactionalDetails=
                transactionalDetailsRepository.findByOrderId(orderId);

        PaymentResponse paymentResponse=
                PaymentResponse.builder()
                        .paymentId(transactionalDetails.getId())
                        .Status(transactionalDetails.getPaymentStatus())
                        .paymentMode(PaymentMode.valueOf(transactionalDetails.getPaymentMode()))
                        .amount(transactionalDetails.getAmount())
                        .orderId(transactionalDetails.getOrderId())
                        .build();
        return paymentResponse;
    }

}
