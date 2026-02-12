package com.DigiClassRoom.OrderService.service;

import com.DigiClassRoom.OrderService.entity.Order;
import com.DigiClassRoom.OrderService.exception.CustomException;
import com.DigiClassRoom.OrderService.external.client.PaymentService;
import com.DigiClassRoom.OrderService.external.client.ProductService;
import com.DigiClassRoom.OrderService.external.request.PaymentRequest;
import com.DigiClassRoom.OrderService.external.response.PaymentResponse;
import com.DigiClassRoom.OrderService.external.response.ProductResponse;
import com.DigiClassRoom.OrderService.model.OrderRequest;
import com.DigiClassRoom.OrderService.model.OrderResponse;
import com.DigiClassRoom.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    RestTemplate restTemplate;

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
                .amount(orderRequest.getTotalAmount())
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

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("get the order details for orderId:{}",orderId);
        Order order= orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException("order not found for orderId:"+orderId,
                        "ORDER_NOT_FOUND",
                        404));

        log.info("fetching the productDetails for orderId:{}",order.getId());
        ProductResponse productResponse= productService.getProductById(order.getProductId()).getBody();

        OrderResponse.ProductDetails productDetails=
                OrderResponse.ProductDetails.builder()
                        .productId(productResponse.getProductId())
                        .name(productResponse.getName())
                        .price(productResponse.getPrice())
                        .quantity(productResponse.getQuantity())
                        .build();

        log.info("fetching payment details for orderId:{}",order.getId());

        PaymentResponse paymentResponse=paymentService.getPaymentDetailsByOrderId(order.getId());
        OrderResponse.PaymentDetails paymentDetails
                =OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .Status(paymentResponse.getStatus())
                .amount(paymentResponse.getAmount())
                .paymentDate(paymentResponse.getPaymentDate())
                .build();

        OrderResponse orderResponse=
                OrderResponse.builder()
                        .orderId(order.getId())
                        .orderDate(order.getOrderDate())
                        .orderStatus(order.getOrderStatus())
                        .amount(order.getAmount())
                        .productDetails(productDetails)
                        .paymentDetails(paymentDetails)
                        .build();
        return orderResponse;
    }

    @Override
    public Long deleteOrderByOrderId(Long orderId) {
        log.info("delete order by orderId:{}",orderId);
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException(
                        "order not found for given orderId:"+orderId,
                        "ORDER_NOT_FOUND",
                        404
                ));
        orderRepository.delete(order);
        return orderId;
    }
}
