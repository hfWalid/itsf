package com.framework.galaxion.domain.model;

import com.framework.galaxion.domain.exception.InvalidOptionForSubscriptionTypeException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Subscription {
    @Setter
    private Long id;
    private final SubscriptionType subscriptionType;
    private final LocalDate subscriptionDate;
    private final String clientId;
    private final List<SubscriptionOption> options;

    public Subscription(
            Long id,
            SubscriptionType subscriptionType,
            LocalDate subscriptionDate,
            String clientId
    ) {
        this.id = id;
        this.subscriptionType = subscriptionType;
        this.subscriptionDate = subscriptionDate;
        this.clientId = clientId;
        this.options = new ArrayList<>();
    }

    public Subscription(
            Long id,
            SubscriptionType subscriptionType,
            LocalDate subscriptionDate,
            String clientId,
            List<SubscriptionOption> options
    ) {
        this.id = id;
        this.subscriptionType = subscriptionType;
        this.subscriptionDate = subscriptionDate;
        this.clientId = clientId;
        this.options = new ArrayList<>(options);
    }

    public SubscriptionOption addOption(OptionName optionName) {
        validateOptionCompatibility(optionName);

        SubscriptionOption option = new SubscriptionOption(
                null,
                optionName,
                LocalDate.now()
        );

        this.options.add(option);
        return option;
    }

    public List<SubscriptionOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    private void validateOptionCompatibility(OptionName optionName) {
        boolean isValid = switch (optionName) {
            case ROAMING -> subscriptionType == SubscriptionType.MOBILE;
            case NETFLIX -> subscriptionType == SubscriptionType.FIBER;
        };

        if (!isValid) {
            throw new InvalidOptionForSubscriptionTypeException(subscriptionType, optionName);
        }
    }
}
