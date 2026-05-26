package io.notification.common.model;

import io.notification.common.enums.Channel;
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
    Channel channel;
    Channel backupChannel;
}
