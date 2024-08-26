package com.gc.services.subscription.controllers.news;

import com.gc.services.subscription.services.news.NewsCategoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.List;

import com.gc.services.subscription.entities.NewsCategory;

@WebMvcTest(NewsCategoriesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NewsCategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsCategoriesService newsCategoriesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNewsCategories() throws Exception {

        List<NewsCategory> categories = Arrays.asList(
                new NewsCategory(1L, "Sports"),
                new NewsCategory(2L, "Politics")
        );

        when(newsCategoriesService.getNewsCategories()).thenReturn(ResponseEntity.ok(categories));

        mockMvc.perform(get("/news/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Sports\"},{\"id\":2,\"name\":\"Politics\"}]"));
    }
}