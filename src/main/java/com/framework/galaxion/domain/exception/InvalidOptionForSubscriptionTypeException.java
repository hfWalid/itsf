package com.framework.galaxion.domain.exception;

import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.SubscriptionType;
import lombok.Getter;

@Getter
public class InvalidOptionForSubscriptionTypeException extends RuntimeException {
    private final SubscriptionType subscriptionType;
    private final OptionName optionName;

    public InvalidOptionForSubscriptionTypeException(
            SubscriptionType subscriptionType,
            OptionName optionName
    ) {
        super(String.format(
                "%s option can only be added to %s subscriptions",
                optionName,
                getAllowedType(optionName)
        ));
        this.subscriptionType = subscriptionType;
        this.optionName = optionName;
    }

    private static SubscriptionType getAllowedType(OptionName optionName) {
        return switch (optionName) {
            case ROAMING -> SubscriptionType.MOBILE;
            case NETFLIX -> SubscriptionType.FIBER;
        };
    }
}
