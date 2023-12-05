package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Priority {

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String priorityLabel;

    Priority(String priorityLabel) {
        this.priorityLabel = priorityLabel;
    }
}
