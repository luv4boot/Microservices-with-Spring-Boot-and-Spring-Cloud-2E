package com.luv4code.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface ProductService {

    /*@GetMapping(value = "/product/{productId}", produces = "application/json")
    Product getProduct(@PathVariable int productId);

    @PostMapping(value = "/product", consumes = "application/json", produces = "application/json")
    Product createProduct(@RequestBody Product product);

    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable("productId") int productId);*/

    Mono<Product> createProduct(Product body);

    @GetMapping(value = "/product/{productId}", produces = "application/json")
    Mono<Product> getProduct(@PathVariable int productId);

    Mono<Void> deleteProduct(int productId);

}
