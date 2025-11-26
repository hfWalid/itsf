package com.framework.galaxion.infra.adapter.in.rest.mapper;

import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionOptionResponse;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SubscriptionRestMapper {

    public SubscriptionResponse toResponse(Subscription domain) {
        return SubscriptionResponse.builder()
                .id(domain.getId())
                .subscriptionType(domain.getSubscriptionType())
                .subscriptionDate(domain.getSubscriptionDate())
                .clientId(domain.getClientId())
                .options(domain.getOptions().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public SubscriptionOptionResponse toResponse(SubscriptionOption domain) {
        return SubscriptionOptionResponse.builder()
                .id(domain.getId())
                .optionName(domain.getOptionName())
                .subscriptionDate(domain.getSubscriptionDate())
                .build();
    }
}

