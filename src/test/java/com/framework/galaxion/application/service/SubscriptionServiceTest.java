package com.framework.galaxion.application.service;

import com.framework.galaxion.domain.exception.InvalidOptionForSubscriptionTypeException;
import com.framework.galaxion.domain.exception.SubscriptionNotFoundException;
import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.domain.model.SubscriptionType;
import com.framework.galaxion.domain.port.out.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Suite Test: Subscription Service Tests")
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @Test
    @DisplayName("Test case: should_return_all_subscriptions_with_options")
    void should_return_all_subscriptions_with_options() {
        Subscription subscription1 = new Subscription(
                1L, SubscriptionType.MOBILE, LocalDate.now(), "C123"
        );
        subscription1.addOption(OptionName.ROAMING);

        Subscription subscription2 = new Subscription(
                2L, SubscriptionType.FIBER, LocalDate.now(), "C456"
        );
        subscription2.addOption(OptionName.NETFLIX);

        when(subscriptionRepository.findAll())
                .thenReturn(List.of(subscription1, subscription2));

        List<Subscription> result = subscriptionService.execute();

        assertThat(result)
                .hasSize(2)
                .extracting(Subscription::getId)
                .containsExactly(1L, 2L);
        assertThat(result.get(0).getOptions()).hasSize(1);
        assertThat(result.get(1).getOptions()).hasSize(1);
        verify(subscriptionRepository).findAll();
    }

    @Test
    @DisplayName("Test case: should_return_empty_list_when_no_subscriptions")
    void should_return_empty_list_when_no_subscriptions() {
        when(subscriptionRepository.findAll()).thenReturn(List.of());

        List<Subscription> result = subscriptionService.execute();

        assertThat(result).isEmpty();
        verify(subscriptionRepository).findAll();
    }

    @Test
    @DisplayName("Test case: should_add_option_to_existing_subscription")
    void should_add_option_to_existing_subscription() {
        Subscription subscription = new Subscription(
                1L, SubscriptionType.MOBILE, LocalDate.now(), "C123"
        );
        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenReturn(subscription);

        SubscriptionOption result = subscriptionService.execute(1L, OptionName.ROAMING);

        assertThat(result).isNotNull();
        assertThat(result.getOptionName()).isEqualTo(OptionName.ROAMING);
        assertThat(result.getSubscriptionDate()).isEqualTo(LocalDate.now());
        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    @DisplayName("Test case: should_throw_exception_when_subscription_not_found")
    void should_throw_exception_when_subscription_not_found() {
        // Given
        when(subscriptionRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> subscriptionService.execute(999L, OptionName.ROAMING))
                .isInstanceOf(SubscriptionNotFoundException.class)
                .hasMessageContaining("999");

        verify(subscriptionRepository).findById(999L);
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test case: should_propagate_domain_exception_when_invalid_option")
    void should_propagate_domain_exception_when_invalid_option() {
        Subscription subscription = new Subscription(
                1L, SubscriptionType.FIBER, LocalDate.now(), "C123"
        );
        when(subscriptionRepository.findById(1L))
                .thenReturn(Optional.of(subscription));

        assertThatThrownBy(() -> subscriptionService.execute(1L, OptionName.ROAMING))
                .isInstanceOf(InvalidOptionForSubscriptionTypeException.class)
                .hasMessageContaining("ROAMING")
                .hasMessageContaining("MOBILE");

        verify(subscriptionRepository).findById(1L);
        verify(subscriptionRepository, never()).save(any());
    }
}
