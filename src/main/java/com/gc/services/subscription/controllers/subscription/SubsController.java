package com.gc.services.subscription.controllers.subscription;


import com.gc.services.subscription.dtos.SubscriptionDTO;
import com.gc.services.subscription.services.subscriptions.SubsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubsController {

    private final SubsService subsService;

    @PostMapping("check-status")
    public String welcome() {
        return "Welcome to subscription service";
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Object> subscribe (@RequestBody SubscriptionDTO subscription) {
        return subsService.createSubscriptions(subscription);
    }

    @GetMapping("/subscriptions/{phoneNumber}")
    public ResponseEntity<Object> getSubscriptions(@PathVariable("phoneNumber") String phoneNumber) {
        return subsService.getSubscriptions(phoneNumber);
    }
}
