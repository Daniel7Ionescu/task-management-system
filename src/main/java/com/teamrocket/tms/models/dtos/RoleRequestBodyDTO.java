package com.teamrocket.tms.models.dtos;

import com.teamrocket.tms.utils.enums.Role;
import lombok.Data;

@Data
public class RoleRequestBodyDTO {

    private Role role;
}
