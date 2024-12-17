package com.candy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/custom-login")
    public String customLoginPage() {
        return "custom-login"; // Returns the "custom-login.html" view
    }
}
