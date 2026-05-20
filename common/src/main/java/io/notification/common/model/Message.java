package io.notification.common.model;

import io.notification.common.enums.MessageType;
import io.notification.common.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    Long userId;
    Event event;
    MessageType messageType;
    Priority priority;
    String messageText;
    String messageSubject;
}
