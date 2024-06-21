package hanaon.AiAssistant.web.controller;
import hanaon.AiAssistant.apiPayload.exception.ResourceNotFoundException;
import hanaon.AiAssistant.domain.ChatHistory;
import hanaon.AiAssistant.domain.User;
import hanaon.AiAssistant.repository.ChatHistoryRepository;
import hanaon.AiAssistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{user_id}/chat-history")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ChatHistory createChatHistory(@PathVariable("user_id") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setStartTime(LocalDateTime.now());
        chatHistory.setUser(user);
        return chatHistoryRepository.save(chatHistory);
    }

    @GetMapping
    public List<ChatHistory> getAllChatHistory(@PathVariable("user_id") Long userId) {
        return chatHistoryRepository.findByUserUserId(userId);
    }

    @GetMapping("/{chat_id}")
    public ChatHistory getChatHistory(@PathVariable("chat_id") Long chatId) {
        return chatHistoryRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("ChatHistory not found"));
    }

    @PutMapping("/{chat_id}")
    public ChatHistory updateChatHistory(@PathVariable("chat_id") Long chatId, @RequestBody ChatHistory chatHistoryDetails) {
        ChatHistory chatHistory = chatHistoryRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("ChatHistory not found"));
        chatHistory.setEndTime(chatHistoryDetails.getEndTime());
        return chatHistoryRepository.save(chatHistory);
    }

    @DeleteMapping("/{chat_id}")
    public ResponseEntity<?> deleteChatHistory(@PathVariable("chat_id") Long chatId) {
        ChatHistory chatHistory = chatHistoryRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("ChatHistory not found"));
        chatHistoryRepository.delete(chatHistory);
        return ResponseEntity.ok().build();
    }
}
