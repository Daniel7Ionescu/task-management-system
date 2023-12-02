package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Priority {

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private final String label;

    Priority(String label) {
        this.label = label;
    }
}
