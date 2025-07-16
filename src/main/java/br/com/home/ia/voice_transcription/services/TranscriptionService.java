package br.com.home.ia.voice_transcription.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.springframework.util.MultiValueMap;

@Service
public class TranscriptionService {
    private final String openAiApiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public TranscriptionService(@Value("${spring.ai.openai.api-key}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    public String transcribeAudio(MultipartFile audioFile) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("audio", audioFile.getOriginalFilename());
            audioFile.transferTo(tempFile);
            FileSystemResource fileResource = new FileSystemResource(tempFile);

            String url = "https://api.openai.com/v1/audio/transcriptions";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openAiApiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Monta o corpo multipart
            MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
            body.add("file", fileResource);
            body.add("model", "whisper-1");
            body.add("response_format", "text");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            return response.getBody();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo de áudio", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
