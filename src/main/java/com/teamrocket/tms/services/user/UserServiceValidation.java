package com.teamrocket.tms.services.user;

import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.utils.enums.Role;

public interface UserServiceValidation {

    void validateUserAlreadyExists(UserDTO userDTO);

    void validateUserAlreadyInATeam(UserDTO userDTO);

    void validateUserRoleCanPerformAction(User user, Role... validRoles);

    User getValidUser(Long userId, String methodName);

    void validateUserNotInATeam(UserDTO userDTO);

    void validateAreUsersEquals(User user, User secondUser);
}