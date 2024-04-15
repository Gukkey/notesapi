package com.gukkey.notesapi.service;

import com.gukkey.notesapi.domain.User;
import com.gukkey.notesapi.model.UserDTO;
import com.gukkey.notesapi.model.res.Response;
import com.gukkey.notesapi.model.res.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
  ResponseEntity<UserResponse> createUser(UserDTO userDTO);

  List<ResponseEntity<UserResponse>> getAllUsers();

  ResponseEntity<UserResponse> getUser(UUID id);

  List<ResponseEntity<Response>> getAllNotes(UUID id);
}
