package com.teamrocket.tms.models.dtos;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorDTO {

    private Map<String, String> message = new HashMap<>();
}
