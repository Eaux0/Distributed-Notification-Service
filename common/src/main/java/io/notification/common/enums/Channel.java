package io.notification.common.enums;

public enum Channel {
    SMS, EMAIL;

    public static boolean doesNotInclude(String value) {
        for (Channel priority : values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean doesNotInclude(Channel value) {
        for (Channel priority : values()) {
            if (priority == value) {
                return false;
            }
        }
        return true;
    }
}
