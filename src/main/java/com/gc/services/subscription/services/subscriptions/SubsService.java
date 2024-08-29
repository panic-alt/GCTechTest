package com.gc.services.subscription.services.subscriptions;

import com.gc.services.subscription.converters.SubscriptionsConverter;
import com.gc.services.subscription.dtos.NewsCategoryDTO;
import com.gc.services.subscription.dtos.SubscriptionDTO;
import com.gc.services.subscription.dtos.UserSubscriptionsListDTO;
import com.gc.services.subscription.entities.NewsCategory;
import com.gc.services.subscription.entities.Subscription;
import com.gc.services.subscription.entities.User;
import com.gc.services.subscription.handlers.ResponseHandler;
import com.gc.services.subscription.repositories.NewsCategoriesRepository;
import com.gc.services.subscription.repositories.SubsRepository;
import com.gc.services.subscription.repositories.UserRepository;
import com.gc.services.subscription.utils.BodyMessage;
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
        try {
            if (subscription.getPhoneNumber() == null || subscription.getPhoneNumber().isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.PHONE_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST, null);
            }

            if (!RegexUtil.validatePhoneNumber(subscription.getPhoneNumber())) {
                return ResponseHandler.generateResponse(BodyMessage.INVALID_PHONE_NUMBER, HttpStatus.BAD_REQUEST, null);
            }

            if (subscription.getSubscriptions() == null || subscription.getSubscriptions().isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.SUBS_REQUIRED, HttpStatus.BAD_REQUEST, null);
            }

            Optional<User> userOptional = userRepository.findByPhoneNumber(subscription.getPhoneNumber());

            if (userOptional.isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            List<Long> subIds = subscription.getSubscriptions();
            List<NewsCategory> newsCategories = newsCategoriesRepository.findAllById(subIds);

            if (newsCategories.size() != subIds.size()) {
                List<Long> foundSubIds = newsCategories.stream()
                        .map(NewsCategory::getId)
                        .toList();

                subIds.removeAll(foundSubIds);

                return ResponseHandler.generateResponse("News category " + subIds + " does not exist", HttpStatus.NOT_FOUND, null);

            }

            List<Subscription> newSubscriptions = newsCategories.stream()
                    .map(newsCategory -> Subscription.builder()
                            .user(userOptional.get())
                            .newsCategory(newsCategory)
                            .build())
                    .toList();

            subsRepository.saveAll(newSubscriptions);

            return ResponseHandler.generateResponse("Thanks for suscribing", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ResponseEntity<Object> getSubscriptions(String phoneNumber) {
        try {
            if (!RegexUtil.validatePhoneNumber(phoneNumber)) {
                return ResponseHandler.generateResponse(BodyMessage.INVALID_PHONE_NUMBER, HttpStatus.BAD_REQUEST, null);
            }

            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

            if (userOpt.isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            User user = userOpt.get();

            List<Subscription> subscriptionList = subsRepository.findAllByUserId(user.getId());

            if (subscriptionList.isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.SUBSCRIPTION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            UserSubscriptionsListDTO dtoResponse =  subscriptionsConverter.entityListToDTOList(subscriptionList);

            return ResponseHandler.generateResponse(BodyMessage.ENTITY_CREATED, HttpStatus.OK,dtoResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ResponseEntity<Object> deleteSubscription(Long subId, String phoneNumber) {
        try {
            if (!RegexUtil.validatePhoneNumber(phoneNumber)) {
                return ResponseHandler.generateResponse(BodyMessage.INVALID_PHONE_NUMBER, HttpStatus.BAD_REQUEST, null);
            }

            Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

            if (userOpt.isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            User user = userOpt.get();

            List<Subscription> subscriptionList = subsRepository.findAllByUserId(user.getId());

            if (subscriptionList.isEmpty()) {
                return ResponseHandler.generateResponse(BodyMessage.SUBSCRIPTION_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            Subscription subToDelete = subscriptionList.stream()
                    .filter(sub -> sub.getId().equals(subId))
                    .findFirst().get();

            subsRepository.delete(subToDelete);

            return ResponseHandler.generateResponse("", HttpStatus.NO_CONTENT, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
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
