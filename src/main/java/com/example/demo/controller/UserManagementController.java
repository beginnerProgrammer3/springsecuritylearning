package com.example.demo.controller;


import com.example.demo.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/v1/users")
public class UserManagementController {

    private static final List<User> USERS= Arrays.asList(
            new User(2,"Mateusz","Skorupa"),
            new User(3,"Magdalena","Jurszewicz")
    );
    @GetMapping
    public List<User> getAllUsers(){
        return USERS;
    }
    @PostMapping
    public void registerNewUser(@RequestBody User user){
        System.out.println(user);
    }
    @DeleteMapping(path = "{Id}")
    public void deleteUser(@PathVariable("Id") Integer id){
        System.out.println(id);
    }

    @PutMapping(path = "{Id}")
    public void updateUser(@PathVariable("Id") Integer id, @RequestBody User user){
        System.out.println(String.format("%s %s",user,id));
    }
}
