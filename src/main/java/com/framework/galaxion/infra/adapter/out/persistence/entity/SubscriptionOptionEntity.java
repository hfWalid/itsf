package com.framework.galaxion.infra.adapter.out.persistence.entity;

import com.framework.galaxion.domain.model.OptionName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscription_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionEntity subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "option_name")
    private OptionName optionName;

    @Column(nullable = false, name = "subscription_date")
    private LocalDate subscriptionDate;
}

