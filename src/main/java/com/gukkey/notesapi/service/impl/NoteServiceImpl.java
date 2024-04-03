package com.gukkey.notesapi.service.impl;

import com.gukkey.notesapi.domain.Note;
import com.gukkey.notesapi.mapper.NoteMapper;
import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;
import com.gukkey.notesapi.repository.NoteRepository;
import com.gukkey.notesapi.service.NoteService;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

  private final NoteRepository noteRepository;
  private final NoteMapper noteMapper;

  @Autowired
  public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
    this.noteRepository = noteRepository;
    this.noteMapper = noteMapper;
  }

  private static final String ID_NULL_ERROR_MESSAGE = "Id cannot be null or invalid";

  private static final String NOTE_NOT_FOUND_MESSAGE = "Note not found";

  private static final String NOTE_FOUND_MESSAGE = "Note(s) found";

  public ResponseEntity<ListResponse> getAllNotes() {
    ListResponse response;
    if (noteRepository.findAll().isEmpty()) {
      response = ListResponse.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
    response =
        ListResponse.builder()
            .status(200)
            .message(NOTE_FOUND_MESSAGE)
            .notes(noteRepository.findAll())
            .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  public ResponseEntity<Response> getNoteById(UUID id) {
    Response response;
    if (id == null) {
      response = Response.builder().status(400).message(ID_NULL_ERROR_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
    Optional<Note> optionalNote = noteRepository.findById(id);
    if (optionalNote.isPresent()) {
      response =
          Response.builder()
              .id(optionalNote.get().getId())
              .createdAt(optionalNote.get().getCreatedAt())
              .updatedAt(optionalNote.get().getUpdatedAt())
              .tags(optionalNote.get().getTags())
              .title(optionalNote.get().getTitle())
              .body(optionalNote.get().getBody())
              .status(200)
              .message("Note found")
              .build();
      return ResponseEntity.status(response.getStatus()).body(response);
    } else {
      response = Response.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
  }

  public ResponseEntity<Response> addNote(NoteDTO noteDTO) {
    Response response;
    Note note = noteMapper.toNote(noteDTO);
    if (note == null) {
      response = Response.builder().status(400).message("Error mapping note from input").build();
      return ResponseEntity.status(400).body(response);
    }
    noteRepository.saveAndFlush(note);
    response =
        Response.builder()
            .status(201)
            .message("Note has been created")
            .id(note.getId())
            .createdAt(note.getCreatedAt())
            .updatedAt(note.getUpdatedAt())
            .tags(note.getTags())
            .title(note.getTitle())
            .body(note.getBody())
            .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @Transactional
  public ResponseEntity<Response> editNote(UUID id, NoteDTO noteDTO) {
    Response response;
    if (id == null) {
      response = Response.builder().status(400).message(ID_NULL_ERROR_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
    Optional<Note> optionalNote = noteRepository.findById(id);
    if (!optionalNote.isPresent()) {
      response = Response.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
    // if there is a better way to edit the note without using reflection or this way, please
    // make a pr.
    Note note = optionalNote.get();
    if (!Objects.isNull(noteDTO.getTitle())
        && !Objects.equals(note.getTitle(), noteDTO.getTitle())) {
      note.setTitle(noteDTO.getTitle());
    }
    if (!Objects.isNull(noteDTO.getTags()) && !Objects.equals(note.getTags(), noteDTO.getTags())) {
      note.setTags(noteDTO.getTags());
    }
    if (!Objects.isNull(noteDTO.getBody()) && !Objects.equals(note.getBody(), noteDTO.getBody())) {
      note.setBody(noteDTO.getBody());
    }
    noteRepository.saveAndFlush(note);
    response =
        Response.builder()
            .id(note.getId())
            .createdAt(note.getCreatedAt())
            .updatedAt(note.getUpdatedAt())
            .tags(note.getTags())
            .title(note.getTitle())
            .body(note.getBody())
            .status(200)
            .message("Note has been updated")
            .build();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  public ResponseEntity<Response> deleteNoteById(UUID id) {
    Response response;
    if (id == null) {
      response = Response.builder().status(400).message(ID_NULL_ERROR_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
    Optional<Note> optionalNote = Optional.empty();
    try {
      optionalNote = noteRepository.findById(id);
      if (optionalNote.isPresent()) {
        noteRepository.deleteById(id);
        response = Response.builder().status(200).message("Note has been deleted").build();
        return ResponseEntity.status(response.getStatus()).body(response);
      } else {
        response = Response.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
        return ResponseEntity.status(response.getStatus()).body(response);
      }
    } catch (NumberFormatException e) {
      response = Response.builder().status(404).message(ID_NULL_ERROR_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    } catch (NullPointerException e1) {
      response = Response.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
      return ResponseEntity.status(response.getStatus()).body(response);
    }
  }

  @Override
  public ResponseEntity<ListResponse> findNotesByFilter(String sort, String by, NoteDTO noteDTO) {
    Example<Note> example = Example.of(noteMapper.toNote(noteDTO));
    List<Note> notes = noteRepository.findAll(example);
    if (notes.isEmpty()) {
      return ResponseEntity.ok(
          ListResponse.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build());
    } else if (sort != null && sort.equals("updatedAt")) {
      return sortNotesByUpdatedAt(by, notes);
    } else if (sort != null && sort.equals("tags")) {
      return sortNotesByTags(by, notes);
    } else if (sort != null && sort.equals("title")) {
      return sortNotesByTitle(by, notes);
    }
    return ResponseEntity.ok(
        ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(notes).build());
  }

  // Sort methods

  private ResponseEntity<ListResponse> sortNotesByUpdatedAt(String by, List<Note> notes) {
    notes.sort(new SortNotesByUpdatedAt());
    if (by != null && by.equals("desc")) {
      notes.sort(new SortNotesByUpdatedAt().reversed());
    }
    return ResponseEntity.ok(
        ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(notes).build());
  }

  private ResponseEntity<ListResponse> sortNotesByTags(String by, List<Note> notes) {
    notes.sort(new SortNotesByTags());
    if (by != null && by.equals("desc")) {
      notes.sort(new SortNotesByTags().reversed());
    }
    return ResponseEntity.ok(
        ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(notes).build());
  }

  private ResponseEntity<ListResponse> sortNotesByTitle(String by, List<Note> notes) {
    notes.sort(new SortNotesByTitle());
    if (by != null && by.equals("desc")) {
      notes.sort(new SortNotesByTitle().reversed());
    }
    return ResponseEntity.ok(
        ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(notes).build());
  }

  // Comparators

  class SortNotesByUpdatedAt implements Comparator<Note> {
    @Override
    public int compare(Note note1, Note note2) {
      return note1.getUpdatedAt().compareTo(note2.getUpdatedAt());
    }
  }

  class SortNotesByTags implements Comparator<Note> {
    @Override
    public int compare(Note note1, Note note2) {
      // yet to be implemented
      return 0;
    }
  }

  class SortNotesByTitle implements Comparator<Note> {
    @Override
    public int compare(Note note1, Note note2) {
      return note1.getTitle().compareToIgnoreCase(note2.getTitle());
    }
  }
}
