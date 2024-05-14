package com.ridesharing.core.dto;

import com.ridesharing.core.model.NotificationType;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationMsg {
    private NotificationType type;
    private Object message;
}
