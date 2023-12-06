package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum Role {

    JUNIOR("Junior"),
    MEDIOR("Medior"),
    SENIOR("Senior"),
    PROJECTMANAGER("ProjectManager"),
    TEAMLEADER("TeamLeader");
    
    private final String roleLabel;


    Role(String roleLabel) {
        this.roleLabel = roleLabel;
    }
}
