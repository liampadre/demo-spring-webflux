package com.example.webflux.app.controller;

import com.example.webflux.app.model.Product;
import com.example.webflux.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class ThymeleafController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({"/products", "/"})
    public String listProducts(Model model) {
        Flux<Product> products = productRepository.findAll();
        model.addAttribute("title", "Product list");
        model.addAttribute("products", products);

        return "list";
    }

    @GetMapping("/products/buffer/elements")
    public String listProductsUsingBufferWithElements(Model model) {
        Flux<Product> products = productRepository.findAll()
            .map(product -> {
                product.setName(product.getName().toUpperCase());
                return product;
            }).delayElements(Duration.ofSeconds(1));
        model.addAttribute("title", "Product list using buffer of elements");
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 1));

        return "list";
    }

    @GetMapping("/products/buffer/chunked")
    public String listProductsUsingChunkedBuffer(Model model) {
        Flux<Product> products = productRepository.findAll()
            .map(product -> {
                product.setName(product.getName().toUpperCase());
                return product;
            }).repeat(2000);
        model.addAttribute("title", "Product list using buffer of bytes");
        model.addAttribute("products", new ReactiveDataDriverContextVariable(products, 1));

        return "chunkedList";
    }
}
