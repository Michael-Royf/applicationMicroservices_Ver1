package com.michael.notification;

import com.michael.amqp.RabbitMQMessageProducer;
import com.michael.notification.config.NotificationConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        scanBasePackages = {
                "com.michael.notification",
                "com.michael.amqp"
        }
)
@EnableEurekaClient
@EnableFeignClients(
        basePackages = "com.michael.clients"
)
public class NotificationApp {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApp.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer, NotificationConfig config) {
//        return args -> {
//            producer.publish("foo",
//                    config.getInternalExchange(),
//                    config.getInternalNotificationRoutingKey());
//        };
//    }

}
