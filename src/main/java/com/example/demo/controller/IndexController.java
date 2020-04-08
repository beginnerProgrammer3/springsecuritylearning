package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class IndexController {
    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable Integer id){

        return new ResponseEntity(
                userService.getUserById(Integer.valueOf(id)), HttpStatus.OK);
    }

}
