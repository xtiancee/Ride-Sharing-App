package com.ridesharing.core.dto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestUserLocDto {
    private String from;
    private String fromType;
    private String userToLocate;
}
