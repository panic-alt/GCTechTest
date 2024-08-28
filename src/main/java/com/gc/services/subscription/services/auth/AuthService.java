package com.gc.services.subscription.services.auth;

import com.gc.services.subscription.dtos.AuthResponseDTO;
import com.gc.services.subscription.dtos.LoginRequestDTO;
import com.gc.services.subscription.dtos.RegisterRequestDTO;
import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.handlers.ResponseHandler;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.services.JwtService;
import com.gc.services.subscription.utils.BodyMessage;
import com.gc.services.subscription.utils.RegexUtil;
import com.gc.services.subscription.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        try {
            if (!validateLogin(loginRequestDTO)) {
                return ResponseHandler.generateResponse(BodyMessage.MISSING_REQUIRED_FIELDS,HttpStatus.BAD_REQUEST, null);
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
            User user =  userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow();
            String token = jwtService.getToken(user);
            AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .token(token)
                    .build();
            return ResponseHandler.generateResponse("Login Successful",HttpStatus.OK,authResponseDTO);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, null);
        }

    }

    private boolean validateLogin(LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO.getUsername() == null || loginRequestDTO.getUsername().isEmpty()) {
            return false;
        }
        return loginRequestDTO.getPassword() != null && !loginRequestDTO.getPassword().isEmpty();
    }

    public ResponseEntity<Object> register(RegisterRequestDTO registerRequestDTO) {
        try {

            if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
                return ResponseHandler.generateResponse(BodyMessage.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST, null);
            }

            if (!validateRegister(registerRequestDTO)) {
                return ResponseHandler.generateResponse(BodyMessage.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST, null);
            }

            if (!RegexUtil.validatePhoneNumber(registerRequestDTO.getPhoneNumber())) {
                return ResponseHandler.generateResponse(BodyMessage.INVALID_PHONE_NUMBER, HttpStatus.BAD_REQUEST, null);
            }

            User newUser = User.builder()
                    .username(registerRequestDTO.getUsername())
                    .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                    .phoneNumber(registerRequestDTO.getPhoneNumber())
                    .role(Role.USER)
                    .build();
            userRepository.save(newUser);

            AuthResponseDTO authResponseDTO = AuthResponseDTO.builder()
                    .username(newUser.getUsername())
                    .phoneNumber(newUser.getPhoneNumber())
                    .token(jwtService.getToken(newUser))
                    .build();
            return ResponseHandler.generateResponse("Registration successful", HttpStatus.OK, authResponseDTO);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    private boolean validateRegister(RegisterRequestDTO registerRequestDTO) {
        if (registerRequestDTO.getUsername() == null || registerRequestDTO.getUsername().isEmpty()) {
            return false;
        }
        if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().isEmpty()) {
            return false;
        }
        return registerRequestDTO.getPhoneNumber() != null && !registerRequestDTO.getPhoneNumber().isEmpty();
    }
}
