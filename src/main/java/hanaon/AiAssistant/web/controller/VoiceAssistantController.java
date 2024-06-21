package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.SpeechToTextService;
import hanaon.AiAssistant.service.TextGenerationService;
import hanaon.AiAssistant.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/voice-assistant")
public class VoiceAssistantController {

    @Autowired
    private SpeechToTextService speechToTextService;

    @Autowired
    private TextGenerationService textGenerationService;

    @Autowired
    private TextToSpeechService textToSpeechService;

    @PostMapping("/process-voice")
    public byte[] processVoice(@RequestParam("voiceFile") MultipartFile voiceFile) {
        try {
            String text = speechToTextService.convertSpeechToText(voiceFile);
            String generatedText = textGenerationService.generateText(text);
            return textToSpeechService.convertTextToSpeech(generatedText);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
