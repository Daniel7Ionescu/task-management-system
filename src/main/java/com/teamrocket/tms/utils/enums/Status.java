package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Status {

    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    AWAITING_REVIEW("Awaiting Review"),
    IN_REVIEW("In Review"),
    DONE("Done");

    private final String statusLabel;

    Status(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
