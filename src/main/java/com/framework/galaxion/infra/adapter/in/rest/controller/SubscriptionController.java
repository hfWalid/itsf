package com.framework.galaxion.infra.adapter.in.rest.controller;

import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.domain.port.in.AddOptionToSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.CreateSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.GetAllSubscriptionsUseCase;
import com.framework.galaxion.infra.adapter.in.rest.dto.AddOptionRequest;
import com.framework.galaxion.infra.adapter.in.rest.dto.CreateSubscriptionRequest;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionOptionResponse;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionResponse;
import com.framework.galaxion.infra.adapter.in.rest.mapper.SubscriptionRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscription Management", description = "APIs for managing subscriptions and options")
public class SubscriptionController {

    private final GetAllSubscriptionsUseCase getAllSubscriptionsUseCase;
    private final CreateSubscriptionUseCase createSubscriptionUseCase;
    private final AddOptionToSubscriptionUseCase addOptionToSubscriptionUseCase;
    private final SubscriptionRestMapper mapper;

    @GetMapping
    @Operation(summary = "Get all subscriptions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subscriptions",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionResponse.class))
                    )
            )
    })
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        List<Subscription> subscriptions = getAllSubscriptionsUseCase.execute();
        List<SubscriptionResponse> response = subscriptions.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "Create a new subscription",
            description = "Creates a new subscription with the specified type and client ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Subscription successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Parameter(description = "Subscription details", required = true)
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        Subscription subscription = createSubscriptionUseCase.execute(
                request.getSubscriptionType(),
                request.getClientId()
        );

        SubscriptionResponse response = mapper.toResponse(subscription);

        return ResponseEntity
                .created(URI.create("/api/subscriptions/" + subscription.getId()))
                .body(response);
    }

    @PostMapping("/{subscriptionId}/options")
    @Operation(summary = "Add option to subscription")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Option successfully added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionOptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid option for subscription type or validation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subscription not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<SubscriptionOptionResponse> addOption(
            @Parameter(description = "Subscription ID", required = true)
            @PathVariable Long subscriptionId,

            @Parameter(description = "Option details to add", required = true)
            @Valid @RequestBody AddOptionRequest request
    ) {
        SubscriptionOption option = addOptionToSubscriptionUseCase.execute(
                subscriptionId,
                request.getOptionName()
        );

        SubscriptionOptionResponse response = mapper.toResponse(option);

        return ResponseEntity
                .created(URI.create("/api/subscriptions/" + subscriptionId + "/options/" + option.getId()))
                .body(response);
    }
}

