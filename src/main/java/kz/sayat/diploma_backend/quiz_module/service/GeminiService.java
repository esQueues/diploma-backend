package kz.sayat.diploma_backend.quiz_module.service;

import kz.sayat.diploma_backend.quiz_module.dto.GeminiRequest;
import kz.sayat.diploma_backend.quiz_module.dto.GeminiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    public String getFeedback(String quizResult) {
        RestTemplate restTemplate = new RestTemplate();
        String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        GeminiRequest request = new GeminiRequest(quizResult);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<GeminiResponse> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, GeminiResponse.class);

        if (response.getBody() != null && response.getBody().getCandidates() != null) {
            List<GeminiResponse.Candidate> candidates = response.getBody().getCandidates();

            if (!candidates.isEmpty() && candidates.get(0).getContent() != null) {
                List<GeminiResponse.Part> parts = candidates.get(0).getContent().getParts();
                if (!parts.isEmpty()) {
                    return parts.get(0).getText();
                }
            }
        }

        return "No feedback available.";
    }



}

