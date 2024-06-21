package hanaon.AiAssistant.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ConversationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private String message;
    private String messageType; // 사용자/시스템
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatHistory chatHistory;

    // getters and setters
}
