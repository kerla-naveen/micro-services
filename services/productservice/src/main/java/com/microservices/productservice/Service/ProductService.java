package com.microservices.productservice.Service;

import com.microservices.productservice.model.ProductRequest;
import com.microservices.productservice.model.ProductResponse;
import com.microservices.productservice.repository.ProductRepository;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
