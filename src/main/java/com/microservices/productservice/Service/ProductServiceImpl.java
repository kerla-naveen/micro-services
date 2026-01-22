package com.microservices.productservice.Service;

import com.microservices.productservice.entity.Product;
import com.microservices.productservice.exception.ProductServiceCustomException;
import com.microservices.productservice.model.ProductRequest;
import com.microservices.productservice.model.ProductResponse;
import com.microservices.productservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Product adding");
        Product product=
                Product.builder()
                        .name(productRequest.getName())
                        .price(productRequest.getPrice())
                        .quantity(productRequest.getQuantity())
                        .build();
        productRepository.save(product);
        log.info("Product added");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
       Product product= productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product not Found with given id","PRODUCT_NOT_FOUND"));

       ProductResponse productResponse= new ProductResponse();
        BeanUtils.copyProperties(product, productResponse );
        return productResponse;
    }
}
