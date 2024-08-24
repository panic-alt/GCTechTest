package com.gc.services.subscription.services.auth;

import com.gc.services.subscription.dtos.AuthResponseDTO;
import com.gc.services.subscription.dtos.LoginRequestDTO;
import com.gc.services.subscription.dtos.RegisterRequestDTO;
import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.services.JwtService;
import com.gc.services.subscription.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        UserDetails user =  userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                .token(token)
                .build();
        return ResponseEntity.ok(authResponseDTO);
    }

    public ResponseEntity<Object> register(RegisterRequestDTO registerRequestDTO) {
        User newUser = User.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .phoneNumber(registerRequestDTO.getPhoneNumber())
                .role(Role.USER)
                .build();
        userRepository.save(newUser);

        AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                .token(jwtService.getToken(newUser))
                .build();

        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
}
