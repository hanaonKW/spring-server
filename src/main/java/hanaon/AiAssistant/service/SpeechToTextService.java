package hanaon.AiAssistant.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class SpeechToTextService {

    public String convertSpeechToText(MultipartFile voiceFile) {
        // 음성 파일을 텍스트로 변환하는 로직 (Google Speech-to-Text API 등 사용)
        return "Converted text";
    }
}

