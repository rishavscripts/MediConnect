package com.mediconnect.backend.service;


import com.mediconnect.backend.dto.MedicationRecommendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OverpassHospitalService {
    @Value("${app.overpass.api-url}")
    private String overpassApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.create();

    public List<MedicationRecommendResponse.HospitalPin> findNearbyHospitals(double lat, double lng, String specialization) {
        // Query hospitals & clinics within 5km radius via OpenStreetMap Overpass API
        String query = String.format("""
            [out:json][timeout:10];
            (
              node["amenity"~"hospital|clinic"](around:5000,%f,%f);
              way["amenity"~"hospital|clinic"](around:5000,%f,%f);
            );
            out center 12;
            """, lat, lng, lat, lng);

        try {
            String response = restClient.post()
                    .uri(overpassApiUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("data=" + query)
                    .retrieve()
                    .body(String.class);

            JsonNode elements = objectMapper.readTree(response).path("elements");
            List<MedicationRecommendResponse.HospitalPin> pins = new ArrayList<>();

            for (JsonNode el : elements) {
                JsonNode tags = el.path("tags");
                String name = tags.path("name").asText("");
                if (name.isBlank()) continue;

                double hLat = el.has("lat") ? el.path("lat").asDouble() : el.path("center").path("lat").asDouble();
                double hLng = el.has("lon") ? el.path("lon").asDouble() : el.path("center").path("lon").asDouble();
                String phone = tags.path("phone").asText(tags.path("contact:phone").asText("108"));
                String street = tags.path("addr:street").asText("");
                String city = tags.path("addr:city").asText("Nearby Healthcare Facility");
                String spec = tags.path("healthcare:speciality").asText(
                        (specialization != null && !specialization.isBlank()) ? specialization : "General Medicine"
                );

                pins.add(MedicationRecommendResponse.HospitalPin.builder()
                        .id(el.path("id").asText())
                        .name(name)
                        .specialization(spec)
                        .address(street.isBlank() ? city : street + ", " + city)
                        .phone(phone)
                        .lat(hLat)
                        .lng(hLng)
                        .build());

                if (pins.size() >= 8) break;
            }

            return pins.isEmpty() ? fallbackHospitals(lat, lng, specialization) : pins;
        } catch (Exception ex) {
            log.warn("Overpass API fallback triggered: {}", ex.getMessage());
            return fallbackHospitals(lat, lng, specialization);
        }
    }

    private List<MedicationRecommendResponse.HospitalPin> fallbackHospitals(double lat, double lng, String specialization) {
        String spec = (specialization != null && !specialization.isBlank()) ? specialization : "General Medicine";
        return List.of(
                MedicationRecommendResponse.HospitalPin.builder()
                        .id("fallback-1")
                        .name("Panihati State General Hospital")
                        .specialization(spec)
                        .address("B.T. Road, Panihati, Kolkata")
                        .phone("+91 33 2553 2211")
                        .lat(lat + 0.005)
                        .lng(lng + 0.004)
                        .build(),
                MedicationRecommendResponse.HospitalPin.builder()
                        .id("fallback-2")
                        .name("Apollo Clinic & Multi-Specialty Center")
                        .specialization(spec)
                        .address("Barrackpore Trunk Road, North 24 Parganas")
                        .phone("+91 33 2592 0100")
                        .lat(lat - 0.004)
                        .lng(lng + 0.006)
                        .build()
        );
    }
}
