package hanaon.AiAssistant.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
public class VoiceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;
    private String filePath;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // getters and setters
}
