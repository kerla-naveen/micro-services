package com.microservices.productservice.model;

import lombok.Data;

@Data
public class ProductResponse {
    private String name;
    private long productId;
    private long price;
    private long quantity;
}
