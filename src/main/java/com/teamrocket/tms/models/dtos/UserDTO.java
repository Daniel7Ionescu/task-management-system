package com.teamrocket.tms.models.dtos;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String role;
}
