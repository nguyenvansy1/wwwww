package com.bookshopping;

import com.bookshopping.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BookShoppingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookShoppingApplication.class, args);
    }

}
