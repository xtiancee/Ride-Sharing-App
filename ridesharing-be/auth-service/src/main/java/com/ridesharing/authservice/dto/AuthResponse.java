package com.ridesharing.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ridesharing.authservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResponse {
    private String access_token;
    private User user;
}
