package com.gukkey.notesapi.repository;

import com.gukkey.notesapi.domain.Note;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    List<Note> findByTags(String[] tags);
    List<Note> findByTitle(String title);
    List<Note> findByTagsAndTitle(String[] tags, String title);
}
