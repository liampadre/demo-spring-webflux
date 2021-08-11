package com.example.webflux.app;

import com.example.webflux.app.model.Product;
import com.example.webflux.app.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class DemoWebfluxApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoWebfluxApplication.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("product").subscribe();
        Flux.just(new Product("TV Samsung Q95T55 QLED", "Appliances", 1599.50),
                new Product("Portátil HP Envy Octacore", "Computers", 1229.99),
                new Product("Papel higiénico Elite 36 unid. triple capa", "Cleaning staff", 7.99),
                new Product("Lavadora LG L1-2020 Wash/Dry Platinum", "Appliances", 749.99))
                .flatMap(product -> {
                    product.setCreateAt(new Date());
                    return productRepository.save(product);
                })
                .subscribe(product -> LOGGER.info(product.getId() + " - " + product.getName()));
    }
}
