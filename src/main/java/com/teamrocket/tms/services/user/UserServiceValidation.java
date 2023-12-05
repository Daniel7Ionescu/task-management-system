package com.teamrocket.tms.services.user;

import com.teamrocket.tms.models.dtos.UserDTO;

public interface UserServiceValidation {

    void validateUserAlreadyExists(UserDTO userDTO);
}
