package edu.concordia;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = "edu.concordia")
public class ContactManagementApplication {


    public static void main(String[] args) {
        SpringApplication.run(ContactManagementApplication.class, args);
    }

}
