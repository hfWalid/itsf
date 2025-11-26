package com.framework.galaxion.infra.adapter.out.persistence;

import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.port.out.SubscriptionRepository;
import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionEntity;
import com.framework.galaxion.infra.adapter.out.persistence.mapper.SubscriptionMapper;
import com.framework.galaxion.infra.adapter.out.persistence.repository.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;
    private final SubscriptionMapper mapper;

    @Override
    public List<Subscription> findAll() {
        return jpaRepository.findAllWithOptions().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return jpaRepository.findByIdWithOptions(id)
                .map(mapper::toDomain);
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = mapper.toEntity(subscription);
        SubscriptionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}

