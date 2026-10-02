
package com.makeupstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MakeupStoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MakeupStoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(MakeupStoreApplication.class);
    }
}
