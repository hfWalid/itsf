package com.framework.galaxion.application.service;

import com.framework.galaxion.domain.exception.SubscriptionNotFoundException;
import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.domain.model.SubscriptionType;
import com.framework.galaxion.domain.port.in.AddOptionToSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.CreateSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.GetAllSubscriptionsUseCase;
import com.framework.galaxion.domain.port.out.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService implements
        GetAllSubscriptionsUseCase,
        AddOptionToSubscriptionUseCase,
        CreateSubscriptionUseCase {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> execute() {
        log.debug("Fetching all subscriptions");
        return subscriptionRepository.findAll();
    }

    @Override
    public SubscriptionOption execute(Long subscriptionId, OptionName optionName) {
        log.debug("Adding option {} to subscription {}", optionName, subscriptionId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        SubscriptionOption option = subscription.addOption(optionName);

        subscriptionRepository.save(subscription);

        log.info("Option {} added to subscription {}", optionName, subscriptionId);

        return option;
    }

    @Override
    public Subscription execute(SubscriptionType subscriptionType, String clientId) {
        log.debug("Creating subscription for client {} with type {}", clientId, subscriptionType);

        Subscription subscription = new Subscription(
                null,
                subscriptionType,
                LocalDate.now(),
                clientId
        );

        Subscription saved = subscriptionRepository.save(subscription);

        log.info("Subscription created with id {} for client {}", saved.getId(), clientId);

        return saved;
    }

}
