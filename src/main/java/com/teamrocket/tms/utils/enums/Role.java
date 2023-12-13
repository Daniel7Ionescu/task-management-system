package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Role {

    JUNIOR("Junior", 1L),
    MEDIOR("Medior", 2L),
    SENIOR("Senior", 3L),
    PROJECT_MANAGER("Project Manager", 4L),
    TEAM_LEADER("Team Leader",5L);
    
    private final String roleLabel;
    private final Long id;

    Role(String roleLabel, Long id) {
        this.roleLabel = roleLabel;
        this.id = id;
    }
}
