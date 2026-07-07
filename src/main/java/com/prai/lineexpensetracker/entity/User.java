package com.prai.lineexpensetracker.entity;

import com.prai.lineexpensetracker.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", length = 225)
    private String displayName;

    @Column(name = "line_user_id", nullable = false, unique = true)
    private String lineUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 225)
    private UserStatus status;

    @Column(name = "connect_at")
    private LocalDate connectedAt;

    @Column(name = "block_at")
    private LocalDate blockedAt;

    @PrePersist
    public void prePersist() {
        if(status == null) {
            status = UserStatus.ACTIVE;
        }

        if ( connectedAt == null) {
            connectedAt = LocalDate.now();
        }
    }




}
