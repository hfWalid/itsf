package com.framework.galaxion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SubscriptionOption {
    private Long id;
    private final OptionName optionName;
    private final LocalDate subscriptionDate;
}
