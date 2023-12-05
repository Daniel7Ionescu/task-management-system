package com.teamrocket.tms.utils.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    JUNIOR("Junior"),
    MEDIOR("Medior"),
    SENIOR("Senior"),
    PROJECTMANAGER("ProjectManager"),
    TEAMLEADER("TeamLeader");
    
    private final String roleLabel;


    RoleEnum(String roleLabel) {
        this.roleLabel = roleLabel;
    }
}
