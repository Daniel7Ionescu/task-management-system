package com.teamrocket.tms.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 25, message = "must be between 3 and 30 characters")
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 25, message = "must be between 3 and 30 characters")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 25, message = "must be between 3 and 30 characters")
    private String role;
}
