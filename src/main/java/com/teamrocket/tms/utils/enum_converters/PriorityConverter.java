package com.teamrocket.tms.utils.enum_converters;

import com.teamrocket.tms.utils.enums.Priority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PriorityConverter implements AttributeConverter<Priority, String> {

    @Override
    public String convertToDatabaseColumn(Priority priority) {
        if (priority == null) {
            return null;
        }

        return priority.getPriorityLabel();
    }

    @Override
    public Priority convertToEntityAttribute(String priorityLabel) {
        if (priorityLabel == null) {
            return null;
        }

        return Stream.of(Priority.values())
                .filter(p -> p.getPriorityLabel().equals(priorityLabel))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
