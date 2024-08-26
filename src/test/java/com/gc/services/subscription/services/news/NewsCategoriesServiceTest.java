package com.gc.services.subscription.services.news;

import com.gc.services.subscription.entities.NewsCategory;
import com.gc.services.subscription.repositories.NewsCategoriesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NewsCategoriesServiceTest {

    @Mock
    private NewsCategoriesRepository newsCategoriesRepository;

    @InjectMocks
    private NewsCategoriesService newsCategoriesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitWhenRepositoryIsEmpty() {
        when(newsCategoriesRepository.count()).thenReturn(0L);

        newsCategoriesService.init();

        verify(newsCategoriesRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testInitWhenRepositoryIsNotEmpty() {
        when(newsCategoriesRepository.count()).thenReturn(5L);

        newsCategoriesService.init();

        verify(newsCategoriesRepository, never()).saveAll(anyList());
    }

    @Test
    void testGetNewsCategories() {
        List<NewsCategory> categories = Arrays.asList(
                new NewsCategory(1L, "Sports"),
                new NewsCategory(2L, "Politics")
        );
        when(newsCategoriesRepository.findAll()).thenReturn(categories);

        ResponseEntity<Object> responseEntity = newsCategoriesService.getNewsCategories();

        assertEquals(ResponseEntity.ok(categories), responseEntity);
        verify(newsCategoriesRepository, times(1)).findAll();
    }
}