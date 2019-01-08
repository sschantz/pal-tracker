package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private String welcomeMessage;

    public WelcomeController(@Value("${WELCOME_MESSAGE:NOT SET}") String message) {
        welcomeMessage = message;
    }

    @GetMapping("/")
    public String sayHello() {
        return welcomeMessage;
    }
}