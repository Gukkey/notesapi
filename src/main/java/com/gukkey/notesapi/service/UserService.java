package com.gukkey.notesapi.service;

import com.gukkey.notesapi.model.UserDTO;
import com.gukkey.notesapi.model.res.UserListResponse;
import com.gukkey.notesapi.model.res.UserResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface UserService {
  ResponseEntity<UserResponse> createUser(UserDTO userDTO);

  ResponseEntity<UserListResponse> getAllUsers();

  ResponseEntity<UserResponse> getUser(UUID id);

}
