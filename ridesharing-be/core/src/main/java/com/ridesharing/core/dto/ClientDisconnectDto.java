package com.ridesharing.core.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ClientDisconnectDto {
    private String clientId;
}
