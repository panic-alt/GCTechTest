package com.gc.services.subscription.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubscriptionDTO {

    String phoneNumber;
    ArrayList<Long> subscriptions;
}
