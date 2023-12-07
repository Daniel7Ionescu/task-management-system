package com.teamrocket.tms.utils.enum_converters;

import com.teamrocket.tms.utils.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }

        return status.getStatusLabel();
    }

    @Override
    public Status convertToEntityAttribute(String statusLabel) {
        if (statusLabel == null) {
            return null;
        }

        return Stream.of(Status.values())
                .filter(s -> s.getStatusLabel().equals(statusLabel))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
