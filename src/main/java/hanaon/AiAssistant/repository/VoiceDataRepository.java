package hanaon.AiAssistant.repository;

import hanaon.AiAssistant.domain.VoiceData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoiceDataRepository extends JpaRepository<VoiceData, Long> {
    List<VoiceData> findByUserUserId(Long userId);
}
