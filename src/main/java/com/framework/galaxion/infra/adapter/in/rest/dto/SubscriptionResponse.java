package com.framework.galaxion.infra.adapter.in.rest.dto;

import com.framework.galaxion.domain.model.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {
    private Long id;
    private SubscriptionType subscriptionType;
    private LocalDate subscriptionDate;
    private String clientId;
    private List<SubscriptionOptionResponse> options;
}
