package com.framework.galaxion.infra.adapter.out.persistence.mapper;

import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionEntity;
import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionOptionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapper {

    public Subscription toDomain(SubscriptionEntity entity) {
        List<SubscriptionOption> options = entity.getOptions().stream()
                .map(this::toDomainOption)
                .collect(Collectors.toList());

        return new Subscription(
                entity.getId(),
                entity.getSubscriptionType(),
                entity.getSubscriptionDate(),
                entity.getClientId(),
                options
        );
    }

    public SubscriptionEntity toEntity(Subscription domain) {
        SubscriptionEntity entity = SubscriptionEntity.builder()
                .id(domain.getId())
                .subscriptionType(domain.getSubscriptionType())
                .subscriptionDate(domain.getSubscriptionDate())
                .clientId(domain.getClientId())
                .build();

        domain.getOptions().forEach(option -> {
            SubscriptionOptionEntity optionEntity = SubscriptionOptionEntity.builder()
                    .id(option.getId())
                    .optionName(option.getOptionName())
                    .subscriptionDate(option.getSubscriptionDate())
                    .build();
            entity.addOption(optionEntity);
        });

        return entity;
    }

    private SubscriptionOption toDomainOption(SubscriptionOptionEntity entity) {
        return new SubscriptionOption(
                entity.getId(),
                entity.getOptionName(),
                entity.getSubscriptionDate()
        );
    }
}
