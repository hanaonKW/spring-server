package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.apiPayload.exception.ResourceNotFoundException;
import hanaon.AiAssistant.domain.User;
import hanaon.AiAssistant.domain.VoiceData;
import hanaon.AiAssistant.repository.UserRepository;
import hanaon.AiAssistant.repository.VoiceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{user_id}/voice-data")
public class VoiceDataController {

    @Autowired
    private VoiceDataRepository voiceDataRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public VoiceData uploadVoiceData(@PathVariable("user_id") Long userId, @RequestParam("file") MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String filePath = saveFile(file); // 파일 저장 로직 구현
        VoiceData voiceData = new VoiceData();
        voiceData.setFilePath(filePath);
        voiceData.setCreatedAt(LocalDateTime.now());
        voiceData.setUser(user);
        return voiceDataRepository.save(voiceData);
    }

    @GetMapping
    public List<VoiceData> getAllVoiceData(@PathVariable("user_id") Long userId) {
        return voiceDataRepository.findByUserUserId(userId);
    }

    @GetMapping("/{data_id}")
    public VoiceData getVoiceData(@PathVariable("data_id") Long dataId) {
        return voiceDataRepository.findById(dataId).orElseThrow(() -> new ResourceNotFoundException("VoiceData not found"));
    }

    @DeleteMapping("/{data_id}")
    public ResponseEntity<?> deleteVoiceData(@PathVariable("data_id") Long dataId) {
        VoiceData voiceData = voiceDataRepository.findById(dataId).orElseThrow(() -> new ResourceNotFoundException("VoiceData not found"));
        voiceDataRepository.delete(voiceData);
        return ResponseEntity.ok().build();
    }

    private String saveFile(MultipartFile file) {
        // 파일 저장 로직 구현
        return "path/to/file";
    }
}
