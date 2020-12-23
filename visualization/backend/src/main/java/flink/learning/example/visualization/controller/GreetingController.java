package flink.learning.example.visualization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import flink.learning.example.visualization.service.GreetingService;

import java.io.IOException;

@RestController
public class GreetingController {
    @Autowired
    private GreetingService greetingService;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam("username") String username) throws IOException {
        return greetingService.greeting(username);
    }
}
