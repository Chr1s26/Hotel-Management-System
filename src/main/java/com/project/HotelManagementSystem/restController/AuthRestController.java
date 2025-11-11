package com.project.HotelManagementSystem.restController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
