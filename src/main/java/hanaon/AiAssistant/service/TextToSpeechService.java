package hanaon.AiAssistant.service;

import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    public byte[] convertTextToSpeech(String text) {
        // 텍스트를 음성 파일로 변환하는 로직 (Google Text-to-Speech API 등 사용)
        return new byte[0];
    }
}

