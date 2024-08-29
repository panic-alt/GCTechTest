package com.gc.services.subscription.converters;

import com.gc.services.subscription.dtos.NewsCategoryDTO;
import com.gc.services.subscription.dtos.UserSubscriptionDTO;
import com.gc.services.subscription.dtos.UserSubscriptionsListDTO;
import com.gc.services.subscription.entities.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionsConverter {

    public UserSubscriptionsListDTO entityListToDTOList(List<Subscription> subscriptionList) {

        List<UserSubscriptionDTO> userSubscriptionDTOList = subscriptionList.stream()
                .map(subscription -> UserSubscriptionDTO.builder()
                        .userSubscriptionId(subscription.getId())
                        .newsCategory(
                                NewsCategoryDTO.builder()
                                        .id(subscription.getNewsCategory().getId())
                                        .name(subscription.getNewsCategory().getName())
                                        .build())
                        .build())
                .toList();

        return UserSubscriptionsListDTO.builder()
                .username(subscriptionList.get(0).getUser().getUsername())
                .phoneNumber(subscriptionList.get(0).getUser().getPhoneNumber())
                .subscriptionsList(userSubscriptionDTOList).build();
    }
}
