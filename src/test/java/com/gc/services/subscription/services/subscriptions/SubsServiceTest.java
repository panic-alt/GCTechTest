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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubsRepository subsRepository;

    @Mock
    private NewsCategoriesRepository newsCategoriesRepository;

    @Mock
    private SubscriptionsConverter subscriptionsConverter;

    @InjectMocks
    private SubsService subsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubscriptions_success() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setPhoneNumber("+1234567890");
        ArrayList<Long> subList = new ArrayList<>();
        subList.add(1L);
        subList.add(2L);
        subscriptionDTO.setSubscriptions(subList);

        User user = new User();
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        NewsCategory category1 = new NewsCategory();
        category1.setId(1L);

        NewsCategory category2 = new NewsCategory();
        category2.setId(2L);

        when(newsCategoriesRepository.findAllById(anyList())).thenReturn(List.of(category1, category2));

        ResponseEntity<Object> response = subsService.createSubscriptions(subscriptionDTO);

        verify(subsRepository, times(1)).saveAll(anyList());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateSubscriptions_userNotFound() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setPhoneNumber("+1234567890");
        ArrayList<Long> subList = new ArrayList<>();
        subList.add(1L);
        subList.add(2L);
        subscriptionDTO.setSubscriptions(subList);

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = subsService.createSubscriptions(subscriptionDTO);

        verify(subsRepository, never()).saveAll(anyList());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("User not found", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testCreateSubscriptions_invalidPhoneNumber() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setPhoneNumber("1234567890");
        ArrayList<Long> subList = new ArrayList<>();
        subList.add(1L);
        subList.add(2L);
        subscriptionDTO.setSubscriptions(subList);

        ResponseEntity<Object> response = subsService.createSubscriptions(subscriptionDTO);

        verify(subsRepository, never()).saveAll(anyList());

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid phone number", body.get("message"));
        assertEquals(400, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testGetSubscriptions_success() {
        String phoneNumber = "+1234567890";
        User user = new User();
        user.setId(1L);

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription());

        when(subsRepository.findAllByUserId(anyLong())).thenReturn(subscriptions);

        UserSubscriptionsListDTO userSubscriptionsListDTO = new UserSubscriptionsListDTO();
        when(subscriptionsConverter.entityListToDTOList(anyList())).thenReturn(userSubscriptionsListDTO);

        ResponseEntity<Object> response = subsService.getSubscriptions(phoneNumber);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Entity created", body.get("message"));
        assertEquals(200, body.get("status"));
        assertNotNull(body.get("data"));
    }

    @Test
    void testGetSubscriptions_userNotFound() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = subsService.getSubscriptions("+1234567890");

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("User not found", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testGetSubscriptions_noSubscriptionsFound() {
        User user = new User();
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        when(subsRepository.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = subsService.getSubscriptions("+1234567890");

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("The user does not have any subscriptions", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testDeleteSubscription_success() {
        String phoneNumber = "+1234567890";
        User user = new User();
        user.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(subsRepository.findAllByUserId(anyLong())).thenReturn(List.of(subscription));

        ResponseEntity<Object> response = subsService.deleteSubscription(1L, phoneNumber);

        verify(subsRepository, times(1)).delete(any(Subscription.class));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteSubscription_subscriptionNotFound() {
        String phoneNumber = "+1234567890";
        User user = new User();
        user.setId(1L);

        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));
        when(subsRepository.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());

        ResponseEntity<Object> response = subsService.deleteSubscription(1L, phoneNumber);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("The user does not have any subscriptions", body.get("message"));
        assertEquals(404, body.get("status"));
        assertNull(body.get("data"));
    }

    @Test
    void testUpdateSubscription_success() {
        Long subId = 1L;
        NewsCategoryDTO newsCategoryDTO = new NewsCategoryDTO();
        newsCategoryDTO.setId(2L);
        newsCategoryDTO.setName("Politics");

        Subscription subscription = new Subscription();
        User user = new User();
        user.setUsername("user");
        user.setPhoneNumber("+1234567890");
        subscription.setUser(user);
        subscription.setId(subId);
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(1L);
        newsCategory.setName("Sports");
        subscription.setNewsCategory(newsCategory);


        when(subsRepository.findById(anyLong())).thenReturn(Optional.of(subscription));

        ResponseEntity<Object> response = subsService.updateSubscription(subId, newsCategoryDTO);

        verify(subsRepository, times(1)).save(subscription);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSubscription_subscriptionNotFound() {
        when(subsRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = subsService.updateSubscription(1L, new NewsCategoryDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Subscription not found", response.getBody());
    }
}