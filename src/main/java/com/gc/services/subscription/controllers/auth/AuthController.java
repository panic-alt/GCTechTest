package com.gc.services.subscription.controllers.auth;



import com.gc.services.subscription.dtos.LoginRequestDTO;
import com.gc.services.subscription.dtos.RegisterRequestDTO;
import com.gc.services.subscription.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Log in with an existing user", description = "Simple username and password login")
    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @Operation(summary = "Register a new user", description = "Phone number must follow pattern +(country code)(area code)(phone number). Spaces, dashes and parenthesis are allowed")
    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

}
