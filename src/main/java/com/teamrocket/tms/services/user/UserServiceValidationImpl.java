package com.teamrocket.tms.services.user;

import com.teamrocket.tms.exceptions.user.UserAlreadyExistsException;
import com.teamrocket.tms.models.dtos.UserDTO;
import com.teamrocket.tms.models.entities.User;
import com.teamrocket.tms.repositories.UserRepository;
import org.springframework.stereotype.Component;

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
}
