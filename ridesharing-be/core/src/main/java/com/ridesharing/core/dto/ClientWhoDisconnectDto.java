package com.ridesharing.core.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientWhoDisconnectDto {
    private String destination;
    private String clientToNotify;
    private String clientId;
}
