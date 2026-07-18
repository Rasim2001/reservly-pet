package com.reservly.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest (
        @NotBlank String email,
        @NotBlank @Size(min = 8, max = 64) String password
){
}
