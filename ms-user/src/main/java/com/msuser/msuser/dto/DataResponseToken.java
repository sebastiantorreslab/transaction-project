package com.msuser.msuser.dto;

import jakarta.validation.constraints.NotNull;

public record DataResponseToken(
                                @NotNull
                                UserResponseDTO user,
                                @NotNull
                                String token) {
}
