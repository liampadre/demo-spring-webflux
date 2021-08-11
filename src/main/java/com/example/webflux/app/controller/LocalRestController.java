package com.example.webflux.app.controller;

import com.example.webflux.app.model.Product;
import com.example.webflux.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/products")
public class LocalRestController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public Flux<Product> listProducts() {
        Flux<Product> products = productRepository.findAll()
            .map(product -> {
                product.setName(product.getName().toUpperCase());
                return product;
            });

        return products;
    }

    @GetMapping("{id}")
    public Mono<Product> listProducts(@PathVariable String id) {
//        Mono<Product> product = productRepository.findById(id);
        Mono<Product> product = productRepository.findAll()
                .filter(p -> p.getId().equals(id))
                .next()
                .map(p -> {
                    p.setName(p.getName().toUpperCase());
                    return p;
                });

        return product;
    }
}
