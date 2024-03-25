package com.gukkey.notesapi;

import com.gukkey.notesapi.model.res.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v0", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthCheckController {

    @GetMapping("/checkhealth")
    public ResponseEntity<Response> checkHealth(){
        Response response = Response.builder().status(HttpStatus.OK.value()).message("OK").build();
        return ResponseEntity.ok(response);
        
    }
}
