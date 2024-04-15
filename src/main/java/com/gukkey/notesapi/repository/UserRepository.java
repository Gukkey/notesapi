package com.gukkey.notesapi.repository;

import com.gukkey.notesapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}
