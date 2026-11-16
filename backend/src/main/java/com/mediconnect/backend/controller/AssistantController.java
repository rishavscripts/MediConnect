package com.mediconnect.backend.controller;

import com.mediconnect.backend.entity.Appointment;
import com.mediconnect.backend.entity.DoseLog;
import com.mediconnect.backend.entity.MedicationSchedule;
import com.mediconnect.backend.repository.AppointmentRepository;
import com.mediconnect.backend.repository.DoseLogRepository;
import com.mediconnect.backend.repository.MedicationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {
    private final MedicationScheduleRepository scheduleRepository;
    private final DoseLogRepository doseLogRepository;
    private final AppointmentRepository appointmentRepository;

    @GetMapping("/schedules")
    public ResponseEntity<List<MedicationSchedule>> getSchedules(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(scheduleRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtAsc(userId));
    }

    @PostMapping("/schedules")
    public ResponseEntity<MedicationSchedule> createSchedule(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody MedicationSchedule body
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        body.setId(null);
        body.setUserId(userId);
        body.setIsActive(true);
        body.setCreatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(scheduleRepository.save(body));
    }

    @PostMapping("/doses/{scheduleId}/log")
    public ResponseEntity<DoseLog> logDose(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID scheduleId,
            @RequestBody Map<String, String> payload
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        // Verify ownership before logging
        scheduleRepository.findByIdAndUserId(scheduleId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found for user"));

        String status = payload.getOrDefault("status", "TAKEN");
        LocalDate today = LocalDate.now();

        DoseLog logEntry = doseLogRepository.findByScheduleIdAndUserIdAndLogDate(scheduleId, userId, today)
                .orElseGet(() -> DoseLog.builder()
                        .scheduleId(scheduleId)
                        .userId(userId)
                        .logDate(today)
                        .build());

        logEntry.setStatus(status);
        logEntry.setLoggedAt(OffsetDateTime.now());
        return ResponseEntity.ok(doseLogRepository.save(logEntry));
    }

    public record AdherenceSummary(
            String date,
            int totalScheduled,
            long takenCount,
            int adherencePercentage,
            List<DoseLog> logs
    ) {}

    @GetMapping("/adherence")
    public AdherenceSummary getTodayAdherence(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<MedicationSchedule> active = scheduleRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtAsc(userId);
        List<DoseLog> todayLogs = doseLogRepository.findByUserIdAndLogDate(userId, LocalDate.now());

        long taken = todayLogs.stream().filter(l -> "TAKEN".equalsIgnoreCase(l.getStatus())).count();
        int total = active.size();
        int percentage = (total == 0) ? 100 : (int) Math.round((taken * 100.0) / total);

        return new AdherenceSummary(
                LocalDate.now().toString(),
                total,
                taken,
                percentage,
                todayLogs
        );
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(appointmentRepository.findByUserIdOrderByAppointmentTimeAsc(userId));
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Appointment body
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        body.setId(null);
        body.setUserId(userId);
        return ResponseEntity.ok(appointmentRepository.save(body));
    }
}
