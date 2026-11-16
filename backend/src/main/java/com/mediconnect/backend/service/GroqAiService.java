package com.mediconnect.backend.service;

import com.mediconnect.backend.dto.MedicationRecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroqAiService {
    @Value("${app.groq.api-key}")
    private String groqApiKey;

    @Value("${app.groq.api-url}")
    private String groqApiUrl;

    @Value("${app.groq.model}")
    private String groqModel;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.create();

    public MedicationRecommendResponse analyzeSymptoms(
            String symptoms,
            String severity,
            List<String> conditions,
            List<String> allergies,
            String customNotes
    ) {
        String systemPrompt = """
            You are a clinical triage & safe OTC medication guidance assistant for MediConnect.
            Cross-check the user's symptoms against their allergies, existing conditions, and custom notes.
            NEVER recommend any medication that conflicts with the user's allergies or conditions.
            If symptoms indicate an emergency (e.g., chest pain, severe dyspnea), advise immediate emergency care instead of OTC drugs.
            Respond ONLY with valid JSON matching this exact schema:
            {
              "matchedSpecialization": "Cardiology | General Medicine | Gastroenterology | Neurology | Orthopedics | Pulmonology | Dermatology",
              "triageUrgency": "Low | Moderate | High - Seek Immediate Care",
              "medications": [
                { "name": "string", "dosage": "string", "frequency": "string" }
              ],
              "foodsToEat": ["string"],
              "foodsToAvoid": ["string"],
              "schedule": [
                { "time": "Morning (8:00 AM)", "note": "string" },
                { "time": "Evening (8:00 PM)", "note": "string" }
              ]
            }
            """;

        String userPrompt = String.format("""
            Symptoms: %s
            Severity: %s
            Existing Conditions: %s
            Known Allergies: %s
            Custom Medical Notes: %s
            """,
                symptoms,
                severity != null ? severity : "Moderate",
                (conditions != null && !conditions.isEmpty()) ? String.join(", ", conditions) : "None",
                (allergies != null && !allergies.isEmpty()) ? String.join(", ", allergies) : "None",
                (customNotes != null && !customNotes.isBlank()) ? customNotes : "None"
        );

        Map<String, Object> requestBody = Map.of(
                "model", groqModel,
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        try {
            String rawResponse = restClient.post()
                    .uri(groqApiUrl)
                    .header("Authorization", "Bearer " + groqApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            JsonNode root = objectMapper.readTree(rawResponse);
            String content = root.path("choices").get(0).path("message").path("content").asText();
            return objectMapper.readValue(content, MedicationRecommendResponse.class);
        } catch (Exception ex) {
            log.error("GroqCloud AI call failed, returning safe clinical fallback: {}", ex.getMessage());
            return buildSafeFallback(symptoms);
        }
    }

    private MedicationRecommendResponse buildSafeFallback(String symptoms) {
        boolean urgent = symptoms != null && symptoms.toLowerCase().matches(".*(chest|breath|faint|stroke).*");
        return MedicationRecommendResponse.builder()
                .matchedSpecialization(urgent ? "Cardiology" : "General Medicine")
                .triageUrgency(urgent ? "High - Seek Immediate Care" : "Moderate")
                .medications(urgent
                        ? List.of(new MedicationRecommendResponse.MedicationItem("Immediate Doctor Evaluation", "Do not self-medicate", "Urgent"))
                        : List.of(new MedicationRecommendResponse.MedicationItem("Paracetamol (if no liver/allergy issue)", "500mg", "After meals, max 3/day")))
                .foodsToEat(new ArrayList<>(List.of("Warm water & ORS hydration", "Light easily digestible meals")))
                .foodsToAvoid(new ArrayList<>(List.of("Oily/spicy food", "Alcohol & excess caffeine")))
                .schedule(List.of(
                        new MedicationRecommendResponse.ScheduleSlot("Morning (8:00 AM)", "Hydrate and follow doctor guidance"),
                        new MedicationRecommendResponse.ScheduleSlot("Evening (8:00 PM)", "Monitor symptoms before rest")
                ))
                .build();
    }
}
