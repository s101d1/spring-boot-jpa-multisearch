package com.example;

import com.example.models.QUser;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Iterator;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return args -> {
            System.out.println("########### Populating the data ###########");
            long id = 1;
            userRepository.save(new User(id++, "John Doe", 20));
            userRepository.save(new User(id++, "Andy Doe", 35));
            userRepository.save(new User(id++, "Lisa Doe", 21));
            userRepository.save(new User(id++, "Jim Hendrix", 30));
            userRepository.save(new User(id++, "Claudia", 10));

            QUser qUser = QUser.user;
            BooleanExpression query = qUser.name.likeIgnoreCase("%doe%");
            Iterable<User> results = userRepository.findAll(query);

            Iterator<User> userIterator = results.iterator();
            int i  = 1;
            while (userIterator.hasNext()) {
                User user = userIterator.next();
                System.out.println("########### User " + i + ": " + user);
                ++i;
            }

            System.out.println("########### Finished populating the data ###########");
        };
    }
}
