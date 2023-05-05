package com.scaler.taskmanager.tasks.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Error {
    @JsonProperty("error_id")
    String errorId;
    @JsonProperty("error_desc")
    String errorDescription;
}
