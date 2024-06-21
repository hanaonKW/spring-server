package hanaon.AiAssistant.repository;

import hanaon.AiAssistant.domain.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findByUserUserId(Long userId);
}

