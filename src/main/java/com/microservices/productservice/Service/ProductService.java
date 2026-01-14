package com.microservices.productservice.Service;

import com.microservices.productservice.model.ProductRequest;
import com.microservices.productservice.repository.ProductRepository;

public interface ProductService {

    long addProduct(ProductRequest productRequest);
}
