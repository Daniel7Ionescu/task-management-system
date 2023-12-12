package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Role {

    JUNIOR("Junior"),
    MEDIOR("Medior"),
    SENIOR("Senior"),
    PROJECT_MANAGER("ProjectManager"),
    TEAM_LEADER("TeamLeader");
    
    private final String roleLabel;

    Role(String roleLabel) {
        this.roleLabel = roleLabel;
    }
}
