package com.framework.galaxion.infra.adapter.out.persistence.entity;

import com.framework.galaxion.domain.model.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(nullable = false, name = "subscription_date")
    private LocalDate subscriptionDate;

    @Column(nullable = false, name = "client_id")
    private String clientId;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubscriptionOptionEntity> options = new ArrayList<>();

    public void addOption(SubscriptionOptionEntity option) {
        options.add(option);
        option.setSubscription(this);
    }
}
