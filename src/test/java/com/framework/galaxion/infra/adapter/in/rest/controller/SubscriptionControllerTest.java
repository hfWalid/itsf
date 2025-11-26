package com.framework.galaxion.infra.adapter.in.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.galaxion.domain.exception.InvalidOptionForSubscriptionTypeException;
import com.framework.galaxion.domain.exception.SubscriptionNotFoundException;
import com.framework.galaxion.domain.model.OptionName;
import com.framework.galaxion.domain.model.Subscription;
import com.framework.galaxion.domain.model.SubscriptionOption;
import com.framework.galaxion.domain.model.SubscriptionType;
import com.framework.galaxion.domain.port.in.AddOptionToSubscriptionUseCase;
import com.framework.galaxion.domain.port.in.GetAllSubscriptionsUseCase;
import com.framework.galaxion.infra.adapter.in.rest.dto.AddOptionRequest;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionOptionResponse;
import com.framework.galaxion.infra.adapter.in.rest.dto.SubscriptionResponse;
import com.framework.galaxion.infra.adapter.in.rest.mapper.SubscriptionRestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@DisplayName("Suite Test: Subscription Rest Controller Tests")
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetAllSubscriptionsUseCase getAllSubscriptionsUseCase;

    @MockBean
    private AddOptionToSubscriptionUseCase addOptionToSubscriptionUseCase;

    @MockBean
    private SubscriptionRestMapper mapper;

    @Test
    @DisplayName("Test case: should_return_200_with_all_subscriptions")
    void should_return_200_with_all_subscriptions() throws Exception {
        Subscription sub1 = new Subscription(
                1L, SubscriptionType.MOBILE, LocalDate.now(), "C123"
        );
        sub1.addOption(OptionName.ROAMING);

        Subscription sub2 = new Subscription(
                2L, SubscriptionType.FIBER, LocalDate.now(), "C456"
        );
        sub2.addOption(OptionName.NETFLIX);

        when(getAllSubscriptionsUseCase.execute())
                .thenReturn(List.of(sub1, sub2));

        when(mapper.toResponse(sub1))
                .thenReturn(SubscriptionResponse.builder()
                        .id(1L)
                        .subscriptionType(SubscriptionType.MOBILE)
                        .subscriptionDate(LocalDate.now())
                        .clientId("C123")
                        .options(List.of(SubscriptionOptionResponse.builder()
                                        .id(1L)
                                        .optionName(OptionName.ROAMING)
                                        .subscriptionDate(LocalDate.now())
                                        .build()
                        ))
                        .build());

        when(mapper.toResponse(sub2))
                .thenReturn(SubscriptionResponse.builder()
                        .id(2L)
                        .subscriptionType(SubscriptionType.FIBER)
                        .subscriptionDate(LocalDate.now())
                        .clientId("C456")
                        .options(List.of(
                                SubscriptionOptionResponse.builder()
                                        .id(2L)
                                        .optionName(OptionName.NETFLIX)
                                        .subscriptionDate(LocalDate.now())
                                        .build()
                        ))
                        .build());

        mockMvc.perform(get("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].clientId", is("C123")))
                .andExpect(jsonPath("$[0].options", hasSize(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].clientId", is("C456")));

        verify(getAllSubscriptionsUseCase).execute();
    }

    @Test
    @DisplayName("Test case: should_return_200_with_empty_array_when_no_subscriptions")
    void should_return_200_with_empty_array_when_no_subscriptions() throws Exception {
        when(getAllSubscriptionsUseCase.execute()).thenReturn(List.of());

        mockMvc.perform(get("/api/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(getAllSubscriptionsUseCase).execute();
    }

    @Test
    @DisplayName("Test case: should_include_options_in_subscription_response")
    void should_include_options_in_subscription_response() throws Exception {
        Subscription subscription = new Subscription(
                1L, SubscriptionType.MOBILE, LocalDate.now(), "C123"
        );
        subscription.addOption(OptionName.ROAMING);
        subscription.addOption(OptionName.ROAMING);

        when(getAllSubscriptionsUseCase.execute())
                .thenReturn(List.of(subscription));

        when(mapper.toResponse(subscription))
                .thenReturn(SubscriptionResponse.builder()
                        .id(1L)
                        .subscriptionType(SubscriptionType.MOBILE)
                        .subscriptionDate(LocalDate.now())
                        .clientId("C123")
                        .options(List.of(SubscriptionOptionResponse.builder()
                                        .optionName(OptionName.ROAMING)
                                        .subscriptionDate(LocalDate.now())
                                        .build(),
                                SubscriptionOptionResponse.builder()
                                        .optionName(OptionName.ROAMING)
                                        .subscriptionDate(LocalDate.now())
                                        .build()
                        ))
                        .build());

        mockMvc.perform(get("/api/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].options", hasSize(2)))
                .andExpect(jsonPath("$[0].options[0].optionName", is("ROAMING")));
    }

    @Test
    @DisplayName("Test case: should_return_201_when_option_added_successfully")
    void should_return_201_when_option_added_successfully() throws Exception {
        AddOptionRequest request = new AddOptionRequest(OptionName.ROAMING);
        SubscriptionOption option = new SubscriptionOption(
                10L, OptionName.ROAMING, LocalDate.now()
        );

        when(addOptionToSubscriptionUseCase.execute(1L, OptionName.ROAMING))
                .thenReturn(option);

        when(mapper.toResponse(option))
                .thenReturn(SubscriptionOptionResponse.builder()
                        .id(10L)
                        .optionName(OptionName.ROAMING)
                        .subscriptionDate(LocalDate.now())
                        .build());

        mockMvc.perform(post("/api/subscriptions/1/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/api/subscriptions/1/options/10")))
                .andExpect(jsonPath("$.optionName", is("ROAMING")))
                .andExpect(jsonPath("$.subscriptionDate", notNullValue()));

        verify(addOptionToSubscriptionUseCase).execute(1L, OptionName.ROAMING);
    }

    @Test
    @DisplayName("Test case: should_return_404_when_subscription_not_found")
    void should_return_404_when_subscription_not_found() throws Exception {
        AddOptionRequest request = new AddOptionRequest(OptionName.ROAMING);

        when(addOptionToSubscriptionUseCase.execute(999L, OptionName.ROAMING))
                .thenThrow(new SubscriptionNotFoundException(999L));

        mockMvc.perform(post("/api/subscriptions/999/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("SUBSCRIPTION_NOT_FOUND")))
                .andExpect(jsonPath("$.message", containsString("999")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(addOptionToSubscriptionUseCase).execute(999L, OptionName.ROAMING);
    }

    @Test
    @DisplayName("Test case: should_return_400_when_request_body_invalid")
    void should_return_400_when_request_body_invalid() throws Exception {
        String invalidRequest = "{}";

        mockMvc.perform(post("/api/subscriptions/1/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.message", containsString("Option name is required")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(addOptionToSubscriptionUseCase, never()).execute(any(), any());
    }
}

