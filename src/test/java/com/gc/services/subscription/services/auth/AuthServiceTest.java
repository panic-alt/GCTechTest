package com.gc.services.subscription.services.auth;

import com.gc.services.subscription.dtos.AuthResponseDTO;
import com.gc.services.subscription.dtos.LoginRequestDTO;
import com.gc.services.subscription.dtos.RegisterRequestDTO;
import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.services.JwtService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_success() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("testUser");
        loginRequestDTO.setPassword("password");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setPhoneNumber("+1234567890");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("token");

        ResponseEntity<Object> response = authService.login(loginRequestDTO);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(jwtService, times(1)).getToken(any(UserDetails.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Login Successful", body.get("message"));
        assertEquals(200, body.get("status"));

        AuthResponseDTO data = (AuthResponseDTO) body.get("data");
        assertNotNull(data);
        assertEquals("testUser", data.getUsername());
        assertEquals("+1234567890", data.getPhoneNumber());
        assertEquals("token", data.getToken());
    }

    @Test
    void testLogin_invalidCredentials() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("testUser");
        loginRequestDTO.setPassword("password");

        doThrow(new RuntimeException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<Object> response = authService.login(loginRequestDTO);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid credentials", body.get("message"));
        assertEquals(401, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testLogin_missingFields() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("");
        loginRequestDTO.setPassword("");

        ResponseEntity<Object> response = authService.login(loginRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Please make sure to complete all fields", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNull(body.get("data"));

    }

    @Test
    void testRegister_success() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("newUser");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setPhoneNumber("+1234567890");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.getToken(any(User.class))).thenReturn("token");

        ResponseEntity<Object> response = authService.register(registerRequestDTO);

        verify(userRepository, times(1)).save(any(User.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Registration successful", body.get("message"));
        assertEquals(200, body.get("status"));

        AuthResponseDTO data = (AuthResponseDTO) body.get("data");
        assertNotNull(data);
        assertEquals("newUser", data.getUsername());
        assertEquals("+1234567890", data.getPhoneNumber());
        assertEquals("token", data.getToken());
    }

    @Test
    void testRegister_userAlreadyExists() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("existingUser");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setPhoneNumber("+1234567890");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        ResponseEntity<Object> response = authService.register(registerRequestDTO);

        verify(userRepository, never()).save(any(User.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("User already exists", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testRegister_missingFields() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("");
        registerRequestDTO.setPassword("");
        registerRequestDTO.setPhoneNumber("");

        ResponseEntity<Object> response = authService.register(registerRequestDTO);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Please make sure to complete all fields", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNull(body.get("data"));


    }

    @Test
    void testRegister_invalidPhoneNumber() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("newUser");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setPhoneNumber("1234567890");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = authService.register(registerRequestDTO);

        verify(userRepository, never()).save(any(User.class));

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid phone number", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNull(body.get("data"));
    }
}