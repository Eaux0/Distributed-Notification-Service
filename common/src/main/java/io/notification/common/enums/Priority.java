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

    public static Integer getPriority(Priority priority) {
        if (priority == Priority.HIGHEST)
            return 2;
        if (priority == Priority.HIGH) // not used for now
            return 2;
        if (priority == Priority.NORMAL)
            return 1;
        if (priority == Priority.LOW) // not used for now
            return 1;
        if (priority == Priority.LOWEST)
            return 0;
        return 0;
    }
}
