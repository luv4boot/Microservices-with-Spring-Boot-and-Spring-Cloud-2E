package com.luv4code.api.core.product;

import org.springframework.web.bind.annotation.*;

public interface ProductService {

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    Product getProduct(@PathVariable int productId);

    @PostMapping(value = "/product", consumes = "application/json", produces = "application/json")
    Product createProduct(@RequestBody Product product);

    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable("productId") int productId);

}
