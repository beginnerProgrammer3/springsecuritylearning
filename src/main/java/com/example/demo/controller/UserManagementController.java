package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/users")
public class UserManagementController {

    private final UserRepository userRepository;

    public UserManagementController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final List<User> USERS= Arrays.asList(
            new User(2,"Mateusz","Skorupa"),
            new User(3,"Magdalena","Jurszewicz")
    );




    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GIRL')")
    public List<User> getAllUsers(){
//        return USERS;
        return userRepository.findAll();
    }

    @GetMapping(path = "{Id}")
    public User getUser(@PathVariable("Id") Integer id){

        return USERS.stream().filter(user -> id.equals(user.getId()))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("User " + id + " does not exist"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('girl:write')")
    User newUser(@RequestBody User user){
        System.out.println(user.toString());
        return userRepository.save(user);
    }
    @DeleteMapping(path = "{Id}")
    @PreAuthorize("hasAuthority('girl:write')")
    public void deleteUser(@PathVariable("Id") Integer id){
        System.out.println(id);
        userRepository.deleteById(id);
    }

    @PutMapping(path = "{Id}")
    @PreAuthorize("hasAuthority('girl:write')")
    public User updateUser(@PathVariable("Id") Integer id, @RequestBody User user){
        System.out.println(String.format("%s %s",id,user));
         return userRepository.findById(id).map(userInRepo -> {
            userInRepo.setFirstName(user.getFirstName());
            userInRepo.setLastName(user.getLastName());
            return userRepository.save(userInRepo);
        }).orElseThrow(() -> new IllegalStateException("User " + id + " does not exist;"));

//                 .orElseGet(() -> {
//            user.setId(id);
//            return userRepository.save(user);
//         });
    }
}

