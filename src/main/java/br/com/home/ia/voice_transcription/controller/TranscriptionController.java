package br.com.home.ia.voice_transcription.controller;

import br.com.home.ia.voice_transcription.services.TranscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transcription")
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    public TranscriptionController(TranscriptionService transcriptionService) {
        this.transcriptionService = transcriptionService;
    }

    @PostMapping("/audio")
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) {
        String transcription = transcriptionService.transcribeAudio(file);
        return ResponseEntity.ok(transcription);
    }
} 