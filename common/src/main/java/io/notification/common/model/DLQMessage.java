package io.notification.common.model;

import io.notification.common.enums.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DLQMessage {
    public String exeption;
    public Channel channel;
    public String userContactInfo;
}
