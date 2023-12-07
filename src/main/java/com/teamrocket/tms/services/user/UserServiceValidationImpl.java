package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.user.UserAlreadyExistsException;
import com.teamrocket.tms.exceptions.user.UserUnauthorizedActionException;
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

    public UserServiceValidationImpl(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public void validateUserAlreadyExists(UserDTO userDTO) {
        User userFound = userRepository.findByEmail(userDTO.getEmail());

        if (userFound != null) {
            throw new UserAlreadyExistsException("A user with the mail " + userDTO.getEmail() + " already exists.");
        }
    }

    @Override
    public void validateUserRoleCanPerformAction(User user, Role...validRoles){
        if(Arrays.stream(validRoles).noneMatch(role -> role == user.getRole())){
            log.info("Invalid User {} : {} with role {} tried to create a Project", user.getId(), user.getLastName(), user.getRole().getRoleLabel());
            throw new UserUnauthorizedActionException("Based on your role, you cannot perform this action");
        }
    }
}
