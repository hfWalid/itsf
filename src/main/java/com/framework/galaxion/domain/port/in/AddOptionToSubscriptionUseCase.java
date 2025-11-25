package com.framework.galaxion.domain.port.in;

import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.SubscriptionOption;

public interface AddOptionToSubscriptionUseCase {
    SubscriptionOption execute(Long subscriptionId, OptionName optionName);
}
