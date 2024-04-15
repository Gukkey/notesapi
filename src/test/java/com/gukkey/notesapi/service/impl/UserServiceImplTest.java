package com.gukkey.notesapi.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.gukkey.notesapi.domain.User;
import com.gukkey.notesapi.mapper.UserMapper;
import com.gukkey.notesapi.model.UserDTO;
import com.gukkey.notesapi.model.res.UserResponse;
import com.gukkey.notesapi.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
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
  UUID exampleNoteId5 = UUID.randomUUID();
  UUID exampleNoteId6 = UUID.randomUUID();

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

  // Tests for getUser()

}
