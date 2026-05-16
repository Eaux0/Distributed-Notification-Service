package io.notification.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    String eventName;
    String origin;
    String channel;
    String backupChannel;
    String messageText;
}
