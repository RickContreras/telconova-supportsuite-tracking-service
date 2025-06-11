package com.telconova.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
    private String path;
    private String error;

    public ErrorResponse(int value, String message2, LocalDateTime now) {
        this.status = value;
        this.message = message2;
        this.timestamp = now;
    }
}
