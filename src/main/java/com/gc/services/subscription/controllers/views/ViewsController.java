package com.gc.services.subscription.controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewsController {

    @GetMapping("api/login")
    public String index() {
        return "login";
    }

    @GetMapping("api/register")
    public String register() {
        return "register";
    }

    @GetMapping("api/home")
    public String home() {
        return "home";
    }

}
