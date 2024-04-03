package com.gukkey.notesapi.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NoteDTO {
    private List<String> tags;
    
    private String title;

    private String body;

    private LocalDateTime updatedAt;

}
