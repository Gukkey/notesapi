package com.gukkey.notesapi.model.res;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class Response {
    int status;
    String message; 
    UUID id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String tag;
    String title;
    String body;
} 
