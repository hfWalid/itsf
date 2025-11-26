package com.framework.galaxion.infra.adapter.out.persistence;

import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionType;
import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionEntity;
import com.framework.galaxion.infra.adapter.out.persistence.entity.SubscriptionOptionEntity;
import com.framework.galaxion.infra.adapter.out.persistence.mapper.SubscriptionMapper;
import com.framework.galaxion.infra.adapter.out.persistence.repository.SubscriptionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import({SubscriptionRepositoryAdapter.class, SubscriptionMapper.class})
@DisplayName("Suite Test: Subscription Repository Adapter Tests")
class SubscriptionRepositoryAdapterTest {

    @Autowired
    private SubscriptionRepositoryAdapter repositoryAdapter;

    @Autowired
    private SubscriptionJpaRepository jpaRepository;

    @Test
    @DisplayName("Test case: should_save_subscription_with_options")
    void should_save_subscription_with_options() {
        Subscription subscription = new Subscription(
                null,
                SubscriptionType.MOBILE,
                LocalDate.of(2025, 1, 15),
                "C123"
        );
        subscription.addOption(OptionName.ROAMING);

        Subscription saved = repositoryAdapter.save(subscription);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOptions()).hasSize(1);

        Optional<SubscriptionEntity> entity = jpaRepository.findById(saved.getId());
        assertThat(entity).isPresent();
        assertThat(entity.get().getOptions()).hasSize(1);
    }

    @Test
    @DisplayName("Test case: should_find_subscription_by_id_with_options")
    void should_find_subscription_by_id_with_options() {
        SubscriptionEntity entity = SubscriptionEntity.builder()
                .subscriptionType(SubscriptionType.FIBER)
                .subscriptionDate(LocalDate.of(2025, 2, 10))
                .clientId("C456")
                .build();

        SubscriptionOptionEntity option = SubscriptionOptionEntity.builder()
                .optionName(OptionName.NETFLIX)
                .subscriptionDate(LocalDate.of(2025, 3, 1))
                .build();

        entity.addOption(option);
        SubscriptionEntity savedEntity = jpaRepository.save(entity);

        Optional<Subscription> result = repositoryAdapter.findById(savedEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedEntity.getId());
        assertThat(result.get().getSubscriptionType()).isEqualTo(SubscriptionType.FIBER);
        assertThat(result.get().getClientId()).isEqualTo("C456");
        assertThat(result.get().getOptions()).hasSize(1);
    }

    @Test
    @DisplayName("Test case: should_return_empty_when_subscription_not_found")
    void should_return_empty_when_subscription_not_found() {
        Optional<Subscription> result = repositoryAdapter.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test case: should_find_all_subscriptions_with_options")
    void should_find_all_subscriptions_with_options() {
        SubscriptionEntity entity1 = SubscriptionEntity.builder()
                .subscriptionType(SubscriptionType.MOBILE)
                .subscriptionDate(LocalDate.now())
                .clientId("C111")
                .build();
        entity1.addOption(SubscriptionOptionEntity.builder()
                .optionName(OptionName.ROAMING)
                .subscriptionDate(LocalDate.now())
                .build());

        SubscriptionEntity entity2 = SubscriptionEntity.builder()
                .subscriptionType(SubscriptionType.FIBER)
                .subscriptionDate(LocalDate.now())
                .clientId("C222")
                .build();

        SubscriptionEntity entity3 = SubscriptionEntity.builder()
                .subscriptionType(SubscriptionType.FIX)
                .subscriptionDate(LocalDate.now())
                .clientId("C333")
                .build();

        jpaRepository.saveAll(List.of(entity1, entity2, entity3));

        List<Subscription> result = repositoryAdapter.findAll();

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Subscription::getClientId)
                .containsExactlyInAnyOrder("C111", "C222", "C333");
        assertThat(result.get(0).getOptions()).isNotNull();
    }

    @Test
    @DisplayName("Test case: should_cascade_save_options_when_saving_subscription")
    void should_cascade_save_options_when_saving_subscription() {
        Subscription subscription = new Subscription(
                null,
                SubscriptionType.MOBILE,
                LocalDate.now(),
                "C999"
        );
        subscription.addOption(OptionName.ROAMING);
        subscription.addOption(OptionName.ROAMING);

        Subscription saved = repositoryAdapter.save(subscription);

        assertThat(saved.getOptions()).hasSize(2);

        SubscriptionEntity entity = jpaRepository.findById(saved.getId()).orElseThrow();
        assertThat(entity.getOptions()).hasSize(2);
        assertThat(entity.getOptions())
                .allMatch(opt -> opt.getId() != null)
                .allMatch(opt -> opt.getOptionName() == OptionName.ROAMING);
    }
}

