package com.gc.services.subscription.repositories;

import com.gc.services.subscription.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubsRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findById(Long id);

    @Query("SELECT s FROM Subscription s JOIN FETCH s.user u JOIN FETCH s.newsCategory nc WHERE u.id = :userId")
    List<Subscription> findAllByUserId(@Param("userId") Long userId);
}
