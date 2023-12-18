package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.user.*;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.UserRepository;
import com.teamrocket.tms.utils.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class UserServiceValidationImpl implements UserServiceValidation {

    private final UserRepository userRepository;

    public UserServiceValidationImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validateUserAlreadyExists(UserDTO userDTO) {
        User userFound = userRepository.findByEmail(userDTO.getEmail());

        if (userFound != null) {
            throw new UserAlreadyExistsException("A user with the mail " + userDTO.getEmail() + " already exists.");
        }
    }

    @Override
    public void validateUserAlreadyInATeam(UserDTO userDTO) {
        if (userDTO.getTeam() != null) {
            throw new UserAlreadyInATeamException("User with name " + userDTO.getFirstName() + " already in a team: " + userDTO.getTeam().getName());
        }
    }

    @Override
    public void validateUserRoleCanPerformAction(User user, Role... validRoles) {
        if (Arrays.stream(validRoles).noneMatch(role -> role == user.getRole())) {
            log.info("User {} : {} with role {} tried action not permitted for this role.", user.getId(), user.getLastName(), user.getRole().getRoleLabel());
            throw new UserUnauthorizedActionException("Based on user role, action cannot be completed.");
        }
    }

    @Override
    public void validateAreUsersEquals(User user, User secondUser) {
        if (user.equals(secondUser)) {
            throw new UsersAreEqualsException("Users are the same.");
        }
    }

    @Override
    public void validateUserNotInATeam(UserDTO userDTO) {
        if (userDTO.getTeam() == null) {
            throw new UserNotInATeamException(userDTO.getFirstName() + " is not in a team. Cannot create task.");
        }
    }

    @Override
    public User getValidUser(Long userId, String methodName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with the id " + userId + " not found."));
        log.info("User with the id {} retrieved. method: {}", userId, methodName);

        return user;
    }
}
