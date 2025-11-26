package com.framework.galaxion.infra.adapter.in.rest.dto;

import com.framework.galaxion.domain.model.OptionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionOptionResponse {
    private Long id;
    private OptionName optionName;
    private LocalDate subscriptionDate;
}

