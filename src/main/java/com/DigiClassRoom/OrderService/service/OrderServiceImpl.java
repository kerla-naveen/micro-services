package com.DigiClassRoom.OrderService.service;

import com.DigiClassRoom.OrderService.entity.Order;
import com.DigiClassRoom.OrderService.external.client.ProductService;
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

    @Override
    public Long placeOrder(OrderRequest orderRequest) {
        // store/place the oder
        log.info("placing the order :{}",orderRequest);
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        log.info("order CREATED :{}",orderRequest);

        Order order= Order.builder()
                .productId(orderRequest.getProductId())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .build();
        orderRepository.save(order);
        log.info("order is placed successfully with orderId:{}",order.getId());
        // lock/reduce qunatity in productService
        // payment service -success/ failure / rejected
        return order.getId();
    }
}
