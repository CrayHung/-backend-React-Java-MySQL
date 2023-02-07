package com.test.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/greetings")
public class GreetingsController {

    //  "/api/v1/greetings"進來的首頁
    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from API");
    }
    //  "/goodbye"進來的頁面
    @GetMapping("/goodbye")
    public ResponseEntity<String> sayGoodBye(){
        return ResponseEntity.ok("goodbye");
    }
    
}
