package com.gukkey.notesapi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserDTO {
  UUID id;
  LocalDateTime createdAt;
  List<UUID> noteId;
}
