package com.teamrocket.tms.utils.enum_converters;

import com.teamrocket.tms.utils.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }

        return role.getRoleLabel();
    }

    @Override
    public Role convertToEntityAttribute(String roleLabel) {
        if (roleLabel == null) {
            return null;
        }

        return Stream.of(Role.values())
                .filter(r -> r.getRoleLabel().equals(roleLabel))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
