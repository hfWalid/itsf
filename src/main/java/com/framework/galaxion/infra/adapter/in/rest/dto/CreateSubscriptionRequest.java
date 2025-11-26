package com.framework.galaxion.infra.adapter.in.rest.dto;

import com.framework.galaxion.domain.model.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionRequest {

    @NotNull(message = "Subscription type is required")
    private SubscriptionType subscriptionType;

    @NotBlank(message = "Client ID is required")
    private String clientId;
}

