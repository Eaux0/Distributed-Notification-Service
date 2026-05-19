package io.notification.common.enums;

public enum Priority {
    HIGHEST, HIGH, NORMAL, LOW, LOWEST;

    public static boolean doesNotInclude(String value) {
        for (Priority priority : values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return false;
            }
        }
        return true;
    }
}
