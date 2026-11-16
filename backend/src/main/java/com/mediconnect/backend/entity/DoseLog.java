package com.mediconnect.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "dose_logs", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "schedule_id", nullable = false)
    private UUID scheduleId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "log_date")
    @Builder.Default
    private LocalDate logDate = LocalDate.now();

    @Column(nullable = false)
    private String status; // TAKEN, MISSED, PENDING

    @Column(name = "logged_at")
    @Builder.Default
    private OffsetDateTime loggedAt = OffsetDateTime.now();
}
