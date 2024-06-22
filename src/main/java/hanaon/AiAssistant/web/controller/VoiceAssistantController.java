package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.SpeechToTextService;
import hanaon.AiAssistant.service.TextGenerationService;
import hanaon.AiAssistant.service.TranslationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private TranslationService translationService;


    @PostMapping("/process-voice")
    public ResponseEntity<String> processVoice(@RequestParam("voiceFile") MultipartFile voiceFile) {
        try {
            // 음성을 텍스트로 변환
            String text = speechToTextService.convertSpeechToText(voiceFile);
            if (text == null || text.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to transcribe audio.");
            }

            // 변환된 텍스트를 콘솔에 출력
            System.out.println("Transcribed Text: " + text);

            // 텍스트를 영어로 번역
            String translatedText = translationService.translateText(text, "KO", "EN");
            System.out.println("Translated Text: " + translatedText);

            // 텍스트를 기반으로 응답 생성
            String generatedText = textGenerationService.generateText(translatedText);
            if (generatedText == null || generatedText.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate text.");
            }

            // 생성된 응답 텍스트를 콘솔에 출력
            System.out.println("Generated Response: " + generatedText);

            // 텍스트를 한글로 번역
            String translatedResponse = translationService.translateText(generatedText, "EN", "KO");
            System.out.println("translated Response: " + translatedResponse);

            // 응답 텍스트 반환
            JSONObject response = new JSONObject();
            response.put("transcribedText", text);
            response.put("generatedText", translatedResponse);

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing voice: " + e.getMessage());
        }
    }
}
