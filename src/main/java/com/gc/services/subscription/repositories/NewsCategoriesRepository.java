package com.gc.services.subscription.repositories;

import com.gc.services.subscription.entities.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoriesRepository extends JpaRepository<NewsCategory, Long> {

}
