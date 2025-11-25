package com.framework.galaxion.domain.port.in;

import com.framework.galaxion.domain.model.Subscription;

import java.util.List;

public interface GetAllSubscriptionsUseCase {
    List<Subscription> execute();
}