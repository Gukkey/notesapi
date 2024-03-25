package com.gukkey.notesapi.repository;

import com.gukkey.notesapi.domain.Note;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTag(String tag);
    List<Note> findByTitle(String title);
    List<Note> findByTagAndTitle(String tag, String title);
}
