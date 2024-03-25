package com.gukkey.notesapi.service.impl;

import com.gukkey.notesapi.domain.Note;
import com.gukkey.notesapi.mapper.NoteMapper;
import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;
import com.gukkey.notesapi.repository.NoteRepository;
import com.gukkey.notesapi.service.NoteService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        response = ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(noteRepository.findAll()).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<Response> getNoteById(Long id) {
        Response response;
        if (id == null) {
            response = Response.builder().status(400).message(ID_NULL_ERROR_MESSAGE).build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            response = Response.builder()
                    .id(optionalNote.get().getId())
                    .createdAt(optionalNote.get().getCreatedAt())
                    .updatedAt(optionalNote.get().getUpdatedAt())
                    .tag(optionalNote.get().getTag())
                    .title(optionalNote.get().getTitle())
                    .body(optionalNote.get().getBody())
                    .status(200)
                    .message("Note found").build();
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
            response = Response.builder().status(400).message("Error mapping note from DTO").build();
            return ResponseEntity.status(400).body(response);
        }
        if (note.getBody() == null || note.getBody().isEmpty()) {
            response = Response.builder().status(400).message("Body cannot be empty").build();
            return ResponseEntity.status(400).body(response);
        }
        noteRepository.save(note);
        response = Response.builder().status(201).message("Note has been created")
                .id(note.getId())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .tag(note.getTag())
                .title(note.getTitle())
                .body(note.getBody()).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Transactional
    public ResponseEntity<Response> editNote(Long id, NoteDTO noteDTO) {
        Response response;
        if (noteDTO.getBody() == ""){
            response = Response.builder().status(400).message("Body cannot be empty").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } else if (id == null) {
            response = Response.builder().status(400).message(ID_NULL_ERROR_MESSAGE).build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (!optionalNote.isPresent()) {
            response = Response.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        // if there is a better way to edit the note without using reflection, please
        // make a pr.
        Note note = optionalNote.get();
        var hasChanges = false;
        if (!Objects.isNull(noteDTO.getTitle()) && !Objects.equals(note.getTitle(), noteDTO.getTitle())) {
            note.setTitle(noteDTO.getTitle());
            hasChanges = true;
        }
        if (!Objects.isNull(noteDTO.getTag()) && !Objects.equals(note.getTag(), noteDTO.getTag())) {
            note.setTag(noteDTO.getTag());
            hasChanges = true;
        }
        if (!Objects.isNull(noteDTO.getBody()) && !Objects.equals(note.getBody(), noteDTO.getBody())) {
            note.setBody(noteDTO.getBody());
            hasChanges = true;
        }
        if (!hasChanges) {
            response = Response.builder().status(400).message("No changes detected").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        } else {
            noteRepository.save(note);
            response = Response.builder()
                    .id(note.getId())
                    .createdAt(note.getCreatedAt())
                    .updatedAt(note.getUpdatedAt())
                    .tag(note.getTag())
                    .title(note.getTitle())
                    .body(note.getBody())
                    .status(200)
                    .message("Note has been updated").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    public ResponseEntity<Response> deleteNoteById(Long id) {
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

    public ResponseEntity<ListResponse> findNotesByTag(String tag) {
        ListResponse response;
        if (tag == null || tag.isEmpty()) {
            response = ListResponse.builder().status(400).message("Tag cannot be empty").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        List<Note> notes = noteRepository.findByTag(tag);
        if (notes.isEmpty()) {
            response = ListResponse.builder().status(404).message(NOTE_NOT_FOUND_MESSAGE).build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = ListResponse.builder().status(200).message(NOTE_FOUND_MESSAGE).notes(notes).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<ListResponse> findNotesByTitle(String title) {
        ListResponse response;
        if (title == null || title.isEmpty()) {
            response = ListResponse.builder().status(400).message("Title cannot be empty").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        List<Note> notes = noteRepository.findByTitle(title);
        if (notes.isEmpty()) {
            response = ListResponse.builder().status(404).message("No notes found").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = ListResponse.builder().status(200).message("Notes found").notes(notes).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<ListResponse> findNotesByTagAndTitle(String tag, String title) {
        ListResponse response;
        if (title == null || tag == null) {
            response = ListResponse.builder().status(400).message("Title or tag cannot be empty").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        List<Note> notes = noteRepository.findByTagAndTitle(tag, title);
        if (notes.isEmpty()) {
            response = ListResponse.builder().status(404).message("No notes found").build();
            return ResponseEntity.status(response.getStatus()).body(response);
        }
        response = ListResponse.builder().status(200).message("Notes found").notes(notes).build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
