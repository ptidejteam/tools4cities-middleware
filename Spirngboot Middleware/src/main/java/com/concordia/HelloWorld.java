package com.concordia;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloWorld {

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }
    @GetMapping("/bye")
    public String sayBye() {
        return "Good bye";
    }
}

