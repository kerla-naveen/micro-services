package com.DigiClassRoom.OrderService.service;

import com.DigiClassRoom.OrderService.model.OrderRequest;

public interface OrderService {
    Long placeOrder(OrderRequest orderRequest);
}
