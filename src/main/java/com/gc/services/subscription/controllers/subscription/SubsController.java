package com.gc.services.subscription.controllers.subscription;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubsController {

    @PostMapping("check-status")
    public String welcome() {
        return "Welcome to subscription service";
    }
}
