package com.microservices.productservice.controller;

import com.microservices.productservice.Service.ProductService;
import com.microservices.productservice.model.ProductRequest;
import com.microservices.productservice.model.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

        @Autowired
        ProductService productService;

        @PostMapping
        ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
            long productId=productService.addProduct(productRequest);
            return new ResponseEntity<>(productId, HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        ResponseEntity<ProductResponse> getProductById(@PathVariable("id") long productId){
            ProductResponse productResponse= productService.getProductById(productId);
            return new ResponseEntity<>(productResponse,HttpStatus.ACCEPTED) ;
        }

        @PutMapping("/reduceQuantity/{id}")
        ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId, @RequestParam long quantity
        ){
            productService.reduceQuantity(productId,quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
}
