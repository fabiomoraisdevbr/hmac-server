package com.example.hmac.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MessageResquestDTO(

        @Valid
        @NotNull
        WeatherStationMessage message,

        @NotNull
        Long timestamp,

        @NotBlank
        String hash
) {}


