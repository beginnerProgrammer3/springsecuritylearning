package com.example.demo.bootstrapData;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootsrap implements CommandLineRunner {

    private UserRepository userRepository;

    public Bootsrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public void run(String... args) throws Exception {

        User user1 = new User(1,"Aneta","Cabalska");
        userRepository.save(user1);
        System.out.println("Added");
    }
}
