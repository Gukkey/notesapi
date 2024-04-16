package com.gukkey.notesapi.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.gukkey.notesapi.domain.User;
import com.gukkey.notesapi.mapper.UserMapper;
import com.gukkey.notesapi.model.UserDTO;
import com.gukkey.notesapi.model.res.UserListResponse;
import com.gukkey.notesapi.model.res.UserResponse;
import com.gukkey.notesapi.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserServiceImpl userService;

  UUID exampleUserId1 = UUID.randomUUID();
  UUID exampleUserId2 = UUID.randomUUID();
  UUID exampleUserId3 = UUID.randomUUID();

  UUID exampleNoteId1 = UUID.randomUUID();
  UUID exampleNoteId2 = UUID.randomUUID();
  UUID exampleNoteId3 = UUID.randomUUID();
  UUID exampleNoteId4 = UUID.randomUUID();

  User user1 =
      User.builder()
          .id(exampleUserId1)
          .createdAt(LocalDateTime.MAX)
          .noteId(List.of(exampleNoteId1))
          .build();
  User user2 =
      User.builder()
          .id(exampleUserId2)
          .createdAt(LocalDateTime.now())
          .noteId(List.of(exampleNoteId2, exampleNoteId3))
          .build();
  User user3 =
      User.builder()
          .id(exampleUserId3)
          .createdAt(LocalDateTime.MIN)
          .noteId(List.of(exampleNoteId4))
          .build();

  // Tests for createUser()

  // Test method if UserId is null

  @Test
  void createUserShouldThrow404IfIdIsNull() {
    // Arrange
    UserDTO emptyIdUserDTO =
        UserDTO.builder()
            .id(null)
            .createdAt(LocalDateTime.MAX)
            .noteId(List.of(exampleNoteId1))
            .build();

    // Act
    ResponseEntity<UserResponse> responseEntity = userService.createUser(emptyIdUserDTO);

    // Assert
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  // Test method if everything is valid, it should return a user response

  @Test
  void createUserShouldExecuteIfDTOIsValid() {
    // Arrange
    UserDTO exampleDTO1 =
        UserDTO.builder()
            .id(exampleUserId1)
            .createdAt(LocalDateTime.MAX)
            .noteId(List.of(exampleNoteId1))
            .build();
    when(userMapper.toUser(exampleDTO1)).thenReturn(user1);
    when(userRepository.save(user1)).thenReturn(user1);

    // Act
    ResponseEntity<UserResponse> responseEntity = userService.createUser(exampleDTO1);

    // Assert
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
  }

  // Tests for getAllUsers()

  // Test method if user lists are empty

  @Test
  void getAllUsersShouldReturn404IfThereAreNoUserLists() {
    // Arrange
    when(userRepository.findAll()).thenReturn(List.of());

    // Act
    ResponseEntity<UserListResponse> responseEntity = userService.getAllUsers();

    // Assert
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  // Test method if the service returns valid user list
  @Test
  void getAllUsersShouldReturnUserListIfThereAreUsers() {
    // Arrange
    when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));

    // Act
    ResponseEntity<UserListResponse> responseEntity = userService.getAllUsers();

    // Assert
    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  // Tests for getUser()

  //  Test method if id is null
  @Test
  void getUserShouldThrow400IfIdIsNull() {
    // Act
    ResponseEntity<UserResponse> responseEntity = userService.getUser(null);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  // Test method is user not found

  @Test
  void getUserShouldThrow404IfUserNotFound() {
    // Arrange
    Optional<User> emptyUser = Optional.empty();
    when(userRepository.findById(exampleUserId1)).thenReturn(emptyUser);

    // Act
    ResponseEntity<UserResponse> responseEntity = userService.getUser(exampleUserId1);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

  // Test method is user is found

  @Test
  void getUserShouldReturnUserIfConditionsAreValid() {
    // Arrange
    Optional<User> optional = Optional.of(user1);
    when(userRepository.findById(exampleUserId1)).thenReturn(optional);

    // Act
    ResponseEntity<UserResponse> responseEntity = userService.getUser(exampleUserId1);

    // Arrange
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
