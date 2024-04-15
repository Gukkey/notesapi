package com.gukkey.notesapi.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class UserResponse {
    int status;
    String message;
    UUID id;
    LocalDateTime createdAt;
    List<UUID> noteId;
}
