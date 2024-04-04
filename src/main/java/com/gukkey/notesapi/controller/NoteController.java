package com.gukkey.notesapi.controller;

import com.gukkey.notesapi.model.NoteDTO;
import com.gukkey.notesapi.model.res.ListResponse;
import com.gukkey.notesapi.model.res.Response;
import com.gukkey.notesapi.service.impl.NoteServiceImpl;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v0/notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NoteController {

  private final NoteServiceImpl noteService;

  @Autowired
  public NoteController(NoteServiceImpl noteService) {
    this.noteService = noteService;
  }

  @GetMapping("/")
  public ResponseEntity<ListResponse> getAllNotes() {
    return noteService.getAllNotes();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Response> getNoteById(@PathVariable UUID id) {
    return noteService.getNoteById(id);
  }

  @PostMapping("/create")
  public ResponseEntity<Response> postNote(@RequestBody NoteDTO noteDTO) {
    return noteService.addNote(noteDTO);
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<Response> editNote(@PathVariable UUID id, @RequestBody NoteDTO noteDTO) {
    return noteService.editNote(id, noteDTO);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Response> deleteNoteById(@PathVariable UUID id) {
    return noteService.deleteNoteById(id);
  }

  @GetMapping("")
  public ResponseEntity<ListResponse> getNotesByFilter(
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) String by,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) LocalDateTime updatedAt) {
    return noteService.findNotesByFilter(
        sort, by, NoteDTO.builder().tag(tag).title(title).updatedAt(updatedAt).build());
        // by is not implemented yet
  }
}
