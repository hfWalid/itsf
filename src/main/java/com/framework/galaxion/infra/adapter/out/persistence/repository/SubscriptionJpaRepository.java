package com.framework.galaxion.infra.adapter.out.persistence.repository;

import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("SELECT s FROM SubscriptionEntity s LEFT JOIN FETCH s.options")
    List<SubscriptionEntity> findAllWithOptions();

    @Query("SELECT s FROM SubscriptionEntity s LEFT JOIN FETCH s.options WHERE s.id = :id")
    Optional<SubscriptionEntity> findByIdWithOptions(Long id);
}

