package com.msuser.msuser.dto;

public record UserResponseDTO(String id,String username, String firstName, String lastName, String email, Boolean emailVerified,
                              Boolean userEnabled, String country) {
}
