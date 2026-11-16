package com.mediconnect.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    private String specialization;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "appointment_time", nullable = false)
    private OffsetDateTime appointmentTime;

    @Builder.Default
    private String status = "UPCOMING";
}
