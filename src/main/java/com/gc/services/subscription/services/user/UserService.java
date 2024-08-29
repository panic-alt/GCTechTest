package com.gc.services.subscription.services.user;

import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.utils.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user}")
    private String adminUser;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.phonenumber}")
    private String adminPhoneNumber;


    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User user = User.builder()
                    .id(1L)
                    .username(adminUser)
                    .password(passwordEncoder.encode(adminPassword))
                    .phoneNumber(adminPhoneNumber)
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(user);

        }
    }
}
