package com.ikram.merchante.demo.controllers;

import com.ikram.merchante.demo.entities.User;
import com.ikram.merchante.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/Hello") public String sayHello() { return "Hello World!"; }

    @GetMapping("") public List<User> getUsers() { return userService.getUsers(); }

    @GetMapping("/{id}") public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
