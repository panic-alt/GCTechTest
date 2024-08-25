package com.gc.services.subscription.repositories;

import com.gc.services.subscription.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubsRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findById(Long id);
}
