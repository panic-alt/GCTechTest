package com.gc.services.subscription.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserSubscriptionsListDTO {
    private String username;
    private String phoneNumber;
    private List<UserSubscriptionDTO> subscriptionsList;
}
