package com.gukkey.notesapi.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;

public interface NoteService {
    ResponseEntity<ListResponse> getAllNotes();
    ResponseEntity<Response> getNoteById(UUID id);
    ResponseEntity<Response> addNote(NoteDTO noteDTO);
    ResponseEntity<Response> editNote(UUID id, NoteDTO noteDTO);
    ResponseEntity<Response> deleteNoteById(UUID id);
    ResponseEntity<ListResponse> findNotesByFilter(String sort, String by, NoteDTO noteDTO);
}
