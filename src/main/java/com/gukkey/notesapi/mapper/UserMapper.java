package com.gukkey.notesapi.mapper;

import com.gukkey.notesapi.domain.User;
import com.gukkey.notesapi.model.UserDTO;

public class UserMapper {
  public User toUser(UserDTO userDTO) {
    User user = new User();
    user.setId(userDTO.getId());
    user.setCreatedAt(userDTO.getCreatedAt());
    user.setNoteId(userDTO.getNoteId());
    return user;
  }
}
