package com.gc.services.subscription.services.news;

import com.gc.services.subscription.entities.NewsCategory;
import com.gc.services.subscription.repositories.NewsCategoriesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCategoriesService {

    private final NewsCategoriesRepository newsCategoriesRepository;

    @PostConstruct
    public void init() {
        if (newsCategoriesRepository.count() == 0) {
            List<NewsCategory> categories = Arrays.asList(
                new NewsCategory(1L,"Sports"),
                new NewsCategory(2L, "Politics"),
                new NewsCategory(3L, "Entertainment"),
                new NewsCategory(4L, "Business"),
                new NewsCategory(5L, "Science"),
                new NewsCategory(6L, "Weather")
            );

            newsCategoriesRepository.saveAll(categories);
        }
    }

    public ResponseEntity<Object> getNewsCategories() {
        List<NewsCategory> categoryList = newsCategoriesRepository.findAll();

        return ResponseEntity.ok(categoryList);
    }
}
