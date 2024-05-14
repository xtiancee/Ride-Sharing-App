package com.ridesharing.core.dto;

import com.ridesharing.core.model.UserType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserDto  {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private UserType type;
}
