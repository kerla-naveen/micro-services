package com.DigiClassRoom.OrderService.service;

import com.DigiClassRoom.OrderService.entity.Order;
import com.DigiClassRoom.OrderService.external.client.PaymentService;
import com.DigiClassRoom.OrderService.external.client.ProductService;
import com.DigiClassRoom.OrderService.external.request.PaymentRequest;
import com.DigiClassRoom.OrderService.model.OrderRequest;
import com.DigiClassRoom.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Override
    public Long placeOrder(OrderRequest orderRequest) {
        // store/place the oder
        log.info("placing the order :{}",orderRequest);
        // lock/reduce quantity in productService
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("order CREATED :{}",orderRequest);

        Order order= Order.builder()
                .productId(orderRequest.getProductId())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .build();
        orderRepository.save(order);
        log.info("order is saved successfully with orderId:{}",order.getId());

        log.info("calling payment service");
        PaymentRequest paymentRequest= PaymentRequest.builder()
                .oderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getQuantity())
                .build();

        String orderStatus=null;
        try {
            paymentService.doPayment(paymentRequest);
            orderStatus="SUCCESS";
            log.info("payment is successful for order id:{}",order.getId());
        } catch (Exception e) {
            orderStatus="FAILED";
            log.info("error in payment for order id:{}",order.getId());
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        // payment service -success/ failure / rejected

        return order.getId();
    }
}
