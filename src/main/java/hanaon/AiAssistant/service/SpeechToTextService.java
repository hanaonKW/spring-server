package hanaon.AiAssistant.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface SpeechToTextService {
    String convertSpeechToText(MultipartFile file) throws IOException, InterruptedException;
}

