package com.gc.services.subscription.dtos;

import com.gc.services.subscription.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponseDTO {
    String username;
    String phoneNumber;
    String token;
}
