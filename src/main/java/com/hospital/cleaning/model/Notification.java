package com.hospital.cleaning.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String message;
    
    private String icon;
    
    @Column(nullable = false)
    private boolean isRead = false;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum NotificationType {
        INFO, WARNING, SUCCESS, DANGER
    }
    
    // Manual getter
    public NotificationType getType() {
        return type;
    }
}
