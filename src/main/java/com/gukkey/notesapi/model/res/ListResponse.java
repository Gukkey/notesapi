package com.gukkey.notesapi.model.res;

import java.util.List;

import com.gukkey.notesapi.domain.Note;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ListResponse {
    int status;
    String message;
    List<Note> notes;
}
