package com.gukkey.notesapi.model.res;

import com.gukkey.notesapi.domain.User;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserListResponse {
  int status;
  String message;
  List<User> userList;
}
