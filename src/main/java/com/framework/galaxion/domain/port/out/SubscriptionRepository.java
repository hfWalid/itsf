package com.framework.galaxion.domain.port.out;

import com.framework.galaxion.domain.model.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    List<Subscription> findAll();
    Optional<Subscription> findById(Long id);
    Subscription save(Subscription subscription);
}
