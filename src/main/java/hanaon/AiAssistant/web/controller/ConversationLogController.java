package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.apiPayload.exception.ResourceNotFoundException;
import hanaon.AiAssistant.domain.ChatHistory;
import hanaon.AiAssistant.domain.ConversationLog;
import hanaon.AiAssistant.repository.ChatHistoryRepository;
import hanaon.AiAssistant.repository.ConversationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chat-history/{chat_id}/conversation-log")
public class ConversationLogController {

    @Autowired
    private ConversationLogRepository conversationLogRepository;

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @PostMapping
    public ConversationLog createConversationLog(@PathVariable("chat_id") Long chatId, @RequestBody ConversationLog logDetails) {
        ChatHistory chatHistory = chatHistoryRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("ChatHistory not found"));
        logDetails.setChatHistory(chatHistory);
        logDetails.setTimestamp(LocalDateTime.now());
        return conversationLogRepository.save(logDetails);
    }

    @GetMapping
    public List<ConversationLog> getAllConversationLogs(@PathVariable("chat_id") Long chatId) {
        return conversationLogRepository.findByChatHistoryChatId(chatId);
    }

    @GetMapping("/{log_id}")
    public ConversationLog getConversationLog(@PathVariable("log_id") Long logId) {
        return conversationLogRepository.findById(logId).orElseThrow(() -> new ResourceNotFoundException("ConversationLog not found"));
    }

    @DeleteMapping("/{log_id}")
    public ResponseEntity<?> deleteConversationLog(@PathVariable("log_id") Long logId) {
        ConversationLog log = conversationLogRepository.findById(logId).orElseThrow(() -> new ResourceNotFoundException("ConversationLog not found"));
        conversationLogRepository.delete(log);
        return ResponseEntity.ok().build();
    }
}
