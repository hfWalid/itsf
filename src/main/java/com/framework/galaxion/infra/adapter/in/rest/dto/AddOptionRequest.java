package com.framework.galaxion.infra.adapter.in.rest.dto;

import com.framework.galaxion.domain.model.OptionName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddOptionRequest {

    @NotNull(message = "Option name is required")
    private OptionName optionName;
}
