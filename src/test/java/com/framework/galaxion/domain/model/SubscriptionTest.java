package com.framework.galaxion.domain.model;

import com.framework.galaxion.domain.exception.InvalidOptionForSubscriptionTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Suite Test: Subscription Tests")
class SubscriptionTest {

    @Test
    @DisplayName("Test case: should_create_subscription_with_valid_data")
    void should_create_subscription_with_valid_data() {
        SubscriptionType type = SubscriptionType.MOBILE;
        LocalDate date = LocalDate.of(2025, 1, 15);
        String clientId = "C123";

        Subscription subscription = new Subscription(null, type, date, clientId);

        assertThat(subscription.getSubscriptionType()).isEqualTo(type);
        assertThat(subscription.getSubscriptionDate()).isEqualTo(date);
        assertThat(subscription.getClientId()).isEqualTo(clientId);
        assertThat(subscription.getOptions()).isNotNull();
    }

    @Test
    @DisplayName("Test case: should_create_subscription_without_options")
    void should_create_subscription_without_options() {
        Subscription subscription = new Subscription(
                null,
                SubscriptionType.FIBER,
                LocalDate.now(),
                "C456"
        );

        assertThat(subscription.getOptions()).isEmpty();
    }

    @Test
    @DisplayName("Test case: should_add_roaming_option_to_mobile_subscription")
    void should_add_roaming_option_to_mobile_subscription() {
        Subscription subscription = new Subscription(
                1L,
                SubscriptionType.MOBILE,
                LocalDate.now(),
                "C123"
        );

        SubscriptionOption option = subscription.addOption(OptionName.ROAMING);

        assertThat(subscription.getOptions()).hasSize(1);
        assertThat(option.getOptionName()).isEqualTo(OptionName.ROAMING);
        assertThat(option.getSubscriptionDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Test case: should_add_netflix_option_to_fiber_subscription")
    void should_add_netflix_option_to_fiber_subscription() {
        Subscription subscription = new Subscription(
                2L,
                SubscriptionType.FIBER,
                LocalDate.now(),
                "C456"
        );

        SubscriptionOption option = subscription.addOption(OptionName.NETFLIX);

        assertThat(subscription.getOptions()).hasSize(1);
        assertThat(option.getOptionName()).isEqualTo(OptionName.NETFLIX);
    }

    @Test
    @DisplayName("Test case: should_throw_exception_when_adding_roaming_to_fiber")
    void should_throw_exception_when_adding_roaming_to_fiber() {
        Subscription subscription = new Subscription(
                3L,
                SubscriptionType.FIBER,
                LocalDate.now(),
                "C789"
        );

        assertThatThrownBy(() -> subscription.addOption(OptionName.ROAMING))
                .isInstanceOf(InvalidOptionForSubscriptionTypeException.class)
                .hasMessageContaining("ROAMING")
                .hasMessageContaining("MOBILE");
    }

    @Test
    @DisplayName("Test case: should_throw_exception_when_adding_netflix_to_fix")
    void should_throw_exception_when_adding_netflix_to_fix() {
        Subscription subscription = new Subscription(
                6L,
                SubscriptionType.FIX,
                LocalDate.now(),
                "C333"
        );

        assertThatThrownBy(() -> subscription.addOption(OptionName.NETFLIX))
                .isInstanceOf(InvalidOptionForSubscriptionTypeException.class);
    }

    @Test
    @DisplayName("Test case: should_allow_multiple_options_on_same_subscription")
    void should_allow_multiple_options_on_same_subscription() {
        Subscription subscription = new Subscription(
                7L,
                SubscriptionType.MOBILE,
                LocalDate.now(),
                "C444"
        );

        SubscriptionOption option1 = subscription.addOption(OptionName.ROAMING);
        SubscriptionOption option2 = subscription.addOption(OptionName.ROAMING);

        assertThat(subscription.getOptions()).hasSize(2);
        assertThat(option1).isNotSameAs(option2);
    }

}

