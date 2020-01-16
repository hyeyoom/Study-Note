package com.github.hyeyoom.springbook.web;

import com.github.hyeyoom.springbook.web.dto.HelloParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping
    public String herrow(HelloParam param) {
        return "hiyo";
    }
}
