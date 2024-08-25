package com.gc.services.subscription.converters;

import com.gc.services.subscription.dtos.NewsCategoryDTO;
import com.gc.services.subscription.dtos.UserSubscriptionDTO;
import com.gc.services.subscription.entities.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionsConverter {

    public UserSubscriptionDTO entityListToDTO(List<Subscription> subscriptionList) {

            List<NewsCategoryDTO> newsCategoryDTOList = subscriptionList.stream()
                    .map(subscription -> NewsCategoryDTO.builder()
                            .id(subscription.getNewsCategory().getId())
                            .name(subscription.getNewsCategory().getName())
                            .build())
                    .toList();


            return UserSubscriptionDTO.builder()
                    .userSubscriptionId(subscriptionList.get(0).getId())
                    .username(subscriptionList.get(0).getUser().getUsername())
                    .phoneNumber(subscriptionList.get(0).getUser().getPhoneNumber())
                    .newsCategories(newsCategoryDTOList)
                    .build();
    }
}
