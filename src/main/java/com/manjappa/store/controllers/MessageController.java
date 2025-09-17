package com.manjappa.store.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

/*    @RequestMapping("/hello")
    public Message sayHello() {
        return new Message("Hello World");
    } */

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello World";
    }

}