package com.framework.galaxion.domain.port.in;

import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionType;

public interface CreateSubscriptionUseCase {
    Subscription execute(SubscriptionType subscriptionType, String clientId);
}
