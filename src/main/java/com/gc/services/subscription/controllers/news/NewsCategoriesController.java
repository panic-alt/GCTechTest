package com.gc.services.subscription.controllers.news;

import com.gc.services.subscription.services.news.NewsCategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "News API")
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsCategoriesController {

    private final NewsCategoriesService newsCategoriesService;

    @Operation(summary = "Get news categories", description = "Method returns all the categories available for subscription")
    @GetMapping("/categories")
    public ResponseEntity<Object> getNewsCategories() {
        return newsCategoriesService.getNewsCategories();
    }

}
