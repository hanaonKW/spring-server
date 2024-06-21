package hanaon.AiAssistant.repository;

import hanaon.AiAssistant.domain.ConversationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversationLogRepository extends JpaRepository<ConversationLog, Long> {
    List<ConversationLog> findByChatHistoryChatId(Long chatId);
}
