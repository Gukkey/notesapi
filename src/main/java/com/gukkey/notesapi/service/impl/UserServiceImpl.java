package com.gukkey.notesapi.service.impl;

import com.gukkey.notesapi.domain.User;
import com.gukkey.notesapi.mapper.UserMapper;
import com.gukkey.notesapi.model.UserDTO;
import com.gukkey.notesapi.model.res.UserListResponse;
import com.gukkey.notesapi.model.res.UserResponse;
import com.gukkey.notesapi.repository.UserRepository;
import com.gukkey.notesapi.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public ResponseEntity<UserResponse> createUser(UserDTO userDTO) {
    UserResponse userResponse;
    if (userDTO.getId() == null) {
      userResponse =
          UserResponse.builder().status(400).message("User ID should not be null").build();
      return ResponseEntity.status(400).body(userResponse);
    }
    User user = userMapper.toUser(userDTO);
    userRepository.save(user);
    userResponse =
        UserResponse.builder()
            .status(201)
            .message("User has been created")
            .id(user.getId())
            .createdAt(user.getCreatedAt())
            .noteId(user.getNoteId())
            .build();
    return ResponseEntity.status(userResponse.getStatus()).body(userResponse);
  }

  public ResponseEntity<UserListResponse> getAllUsers() {
    UserListResponse userListResponse;
    List<User> userList = userRepository.findAll();
    if (userList.isEmpty()) {
      userListResponse = UserListResponse.builder().status(404).message("Users not found").build();
    } else {
      userListResponse =
          UserListResponse.builder().status(200).message("Notes found").userList(userList).build();
    }
    return ResponseEntity.status(userListResponse.getStatus()).body(userListResponse);
  }

  public ResponseEntity<UserResponse> getUser(UUID id) {
    UserResponse userResponse;
    if (id != null) {
      Optional<User> optional = userRepository.findById(id);
      if (optional.isPresent()) {
        userResponse =
            UserResponse.builder()
                .status(200)
                .message("User Found")
                .id(optional.get().getId())
                .createdAt(optional.get().getCreatedAt())
                .noteId(optional.get().getNoteId())
                .build();
      } else {
        userResponse = UserResponse.builder().status(404).message("User not found").build();
      }
      return ResponseEntity.status(userResponse.getStatus()).body(userResponse);
    }
    userResponse = UserResponse.builder().status(400).message("Id is null").build();
    return ResponseEntity.status(userResponse.getStatus()).body(userResponse);
  }
}
