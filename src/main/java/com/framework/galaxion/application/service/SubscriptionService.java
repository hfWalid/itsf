package com.framework.galaxion.application.service;

import com.framework.galaxion.domain.exception.SubscriptionNotFoundException;
import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.domain.port.in.AddOptionToSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.GetAllSubscriptionsUseCase;
import com.framework.galaxion.domain.port.out.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SubscriptionService implements
        GetAllSubscriptionsUseCase,
        AddOptionToSubscriptionUseCase {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> execute() {
        return subscriptionRepository.findAll();
    }

    @Override
    public SubscriptionOption execute(Long subscriptionId, OptionName optionName) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        SubscriptionOption option = subscription.addOption(optionName);

        subscriptionRepository.save(subscription);

        return option;
    }
}
