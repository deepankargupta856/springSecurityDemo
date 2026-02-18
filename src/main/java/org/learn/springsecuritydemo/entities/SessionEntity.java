package org.learn.springsecuritydemo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sessionId;

    private String refreshToken;

    @ManyToOne
    private User user;

    @CreationTimestamp
    @Column(updatable = true)
    private LocalDateTime lastUsedAt;
}
