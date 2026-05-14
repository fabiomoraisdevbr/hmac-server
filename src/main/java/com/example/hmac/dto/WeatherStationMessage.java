package com.example.hmac.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record WeatherStationMessage (

        @NotBlank
        String temperature,

        @NotBlank
        String humidity,

        @NotBlank
        String pressure

) {}