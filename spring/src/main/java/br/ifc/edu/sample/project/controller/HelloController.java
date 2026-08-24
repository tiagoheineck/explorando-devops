package br.ifc.edu.sample.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String hello() {
        return "Bem-vindo à minha aplicação Spring Boot!!";
    }
    
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World 2!";
    }
}