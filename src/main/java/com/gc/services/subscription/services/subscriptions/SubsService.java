package com.gc.services.subscription.services.subscriptions;

import com.gc.services.subscription.converters.SubscriptionsConverter;
import com.gc.services.subscription.dtos.NewsCategoryDTO;
import com.gc.services.subscription.dtos.SubscriptionDTO;
import com.gc.services.subscription.dtos.UserSubscriptionsListDTO;
import com.gc.services.subscription.entities.NewsCategory;
import com.gc.services.subscription.entities.Subscription;
import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.repositories.NewsCategoriesRepository;
import com.gc.services.subscription.repositories.SubsRepository;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.utils.RegexUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubsService {

    private final UserRepository userRepository;
    private final SubsRepository subsRepository;
    private final NewsCategoriesRepository newsCategoriesRepository;
    private final SubscriptionsConverter  subscriptionsConverter;


    public ResponseEntity<Object> createSubscriptions(SubscriptionDTO subscription) {

        if (subscription.getPhoneNumber() == null || subscription.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }

        if (!RegexUtil.validatePhoneNumber(subscription.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Invalid phone number");
        }

        if (subscription.getSubscriptions() == null || subscription.getSubscriptions().isEmpty()) {
            return ResponseEntity.badRequest().body("Subscriptions are required");
        }

        Optional<User> userOptional = userRepository.findByPhoneNumber(subscription.getPhoneNumber());

        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<Long> subIds = subscription.getSubscriptions();
        List<NewsCategory> newsCategories = newsCategoriesRepository.findAllById(subIds);

        if (newsCategories.size() != subIds.size()) {
            List<Long> foundSubIds = newsCategories.stream()
                    .map(NewsCategory::getId)
                    .toList();

            subIds.removeAll(foundSubIds);

            return new ResponseEntity<>("News category " + subIds + " does not exist", HttpStatus.NOT_FOUND);

        }

        List<Subscription> newSubscriptions = newsCategories.stream()
                .map(newsCategory -> Subscription.builder()
                        .user(userOptional.get())
                        .newsCategory(newsCategory)
                        .build())
                .toList();

        subsRepository.saveAll(newSubscriptions);

        return ResponseEntity.ok("ok");
    }

    public ResponseEntity<Object> getSubscriptions(String phoneNumber) {
        if (!RegexUtil.validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Invalid phone number");
        }

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();

        List<Subscription> subscriptionList = subsRepository.findAllByUserId(user.getId());

        if (subscriptionList.isEmpty()) {
            return new ResponseEntity<>("The user does not have any subscriptions", HttpStatus.NOT_FOUND);
        }

        UserSubscriptionsListDTO dtoResponse =  subscriptionsConverter.entityListToDTOList(subscriptionList);

        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteSubscription(Long subId, String phoneNumber) {
        if (!RegexUtil.validatePhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("Invalid phone number");
        }

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();

        List<Subscription> subscriptionList = subsRepository.findAllByUserId(user.getId());

        Subscription subToDelete = subscriptionList.stream()
                .filter(sub -> sub.getId().equals(subId))
                .findFirst().get();

        subsRepository.delete(subToDelete);

        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<Object> updateSubscription(Long subId, NewsCategoryDTO newsCategory) {

        Subscription subToUpdate = subsRepository.findById(subId).orElse(null);

        if (subToUpdate == null) {
            return new ResponseEntity<>("Subscription not found", HttpStatus.NOT_FOUND);
        }

        subToUpdate.getNewsCategory().setId(newsCategory.getId());
        subToUpdate.getNewsCategory().setName(newsCategory.getName());

        subsRepository.save(subToUpdate);

        return ResponseEntity.ok("ok");
    }
}
