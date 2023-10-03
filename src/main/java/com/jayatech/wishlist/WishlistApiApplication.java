package com.jayatech.wishlist;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMongock
@SpringBootApplication
public class WishlistApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WishlistApiApplication.class, args);
    }
}
