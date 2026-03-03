package com.DigiClassRoom.OrderService.service;

import com.DigiClassRoom.OrderService.model.OrderRequest;
import com.DigiClassRoom.OrderService.model.OrderResponse;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);

    Long deleteOrderByOrderId(Long orderId);
}
