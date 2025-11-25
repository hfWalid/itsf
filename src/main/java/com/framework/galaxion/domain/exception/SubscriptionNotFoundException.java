package com.framework.galaxion.domain.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long subscriptionId) {
        super("Subscription not found with id: " + subscriptionId);
    }
}