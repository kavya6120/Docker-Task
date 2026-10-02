
package com.makeupstore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

interface ProductRepository extends JpaRepository<Product, Long> {
}

interface OrderRepository extends JpaRepository<MakeupOrder, Long> {
}

@RestController
@RequestMapping("/api")
class StoreController {

    private final ProductRepository products;
    private final OrderRepository orders;

    StoreController(ProductRepository products, OrderRepository orders) {
        this.products = products;
        this.orders = orders;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return products.findAll();
    }

    @PostMapping("/orders")
    public MakeupOrder placeOrder(@RequestBody OrderRequest request) {
        if (request.customerName == null
                || request.customerName.trim().isEmpty()
                || request.quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid order details");
        }

        Product product = products.findById(request.productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));

        if (product.getStock() < request.quantity) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient stock");
        }

        BigDecimal total = product.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity));

        product.setStock(product.getStock() - request.quantity);
        products.save(product);

        return orders.save(new MakeupOrder(
                request.customerName.trim(),
                product.getName(),
                request.quantity,
                total
        ));
    }

    @GetMapping("/orders")
    public List<MakeupOrder> getOrders() {
        return orders.findAll();
    }

    static class OrderRequest {
        public String customerName;
        public Long productId;
        public int quantity;
    }
}

@Configuration
class ProductData {

    @Bean
    CommandLineRunner seedProducts(ProductRepository products) {
        return args -> {
            if (products.count() == 0) {
                products.save(new Product(
                        "Rose Lipstick", new BigDecimal("499.00"), 20));
                products.save(new Product(
                        "Liquid Foundation", new BigDecimal("899.00"), 15));
                products.save(new Product(
                        "Volume Mascara", new BigDecimal("349.00"), 25));
                products.save(new Product(
                        "Blush Palette", new BigDecimal("599.00"), 10));
            }
        };
    }
}
