package com.teamrocket.tms.utils.enum_converters;

import com.teamrocket.tms.utils.enums.RoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleEnumConverter implements AttributeConverter<RoleEnum, String> {

    @Override
    public String convertToDatabaseColumn(RoleEnum role) {
        if (role == null) {
            return null;
        }

        return role.getRoleLabel();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String roleLabel) {
        if (roleLabel == null) {
            return null;
        }

        return Stream.of(RoleEnum.values())
                .filter(r -> r.getRoleLabel().equals(roleLabel))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
