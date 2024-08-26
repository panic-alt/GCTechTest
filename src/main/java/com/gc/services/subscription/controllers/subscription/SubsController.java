package com.gc.services.subscription.controllers.subscription;


import com.gc.services.subscription.dtos.SubscriptionDTO;
import com.gc.services.subscription.services.subscriptions.SubsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@Tag(name = "Subscriptions API")
@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubsController {

    private final SubsService subsService;

    @Operation(summary = "Subscribe an user to news categories", description = "Phone number must follow pattern +(country code)(area code)(phone number). Spaces, dashes and parenthesis are allowed. Existing categories can be queried in endpoint /news/categories.")
    @PostMapping("/subscriptions")
    public ResponseEntity<Object> subscribe (@RequestBody SubscriptionDTO subscription) {
        return subsService.createSubscriptions(subscription);
    }

    @Operation(summary = "Query for an user subscriptions",description = "Phone number must follow pattern +(country code)(area code)(phone number). Spaces, dashes and parenthesis are allowed")
    @GetMapping("/subscriptions/{phoneNumber}")
    public ResponseEntity<Object> getSubscriptions(@PathVariable("phoneNumber") String phoneNumber) {
        return subsService.getSubscriptions(phoneNumber);
    }
}
