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
public class UserSubscriptionDTO {

    Long userSubscriptionId;
    NewsCategoryDTO newsCategory;
}
