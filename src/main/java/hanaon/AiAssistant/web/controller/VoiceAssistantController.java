package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.SpeechToTextService;
import hanaon.AiAssistant.service.TextGenerationService;
import hanaon.AiAssistant.service.TextToSpeechService;
import hanaon.AiAssistant.service.TranslationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/voice-assistant")
public class VoiceAssistantController {

    private static final Logger logger = Logger.getLogger(VoiceAssistantController.class.getName());


    @Autowired
    private SpeechToTextService speechToTextService;

    @Autowired
    private TextGenerationService textGenerationService;

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Autowired
    private TranslationService translationService;


    @PostMapping("/process-voice")
    public ResponseEntity<String> processVoice(@RequestParam("voiceFile") MultipartFile voiceFile) {
        try {
            // 파일 정보 로그 출력
            logger.info("Received file: " + voiceFile.getOriginalFilename() + ", size: " + voiceFile.getSize());

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

            // 최종 응답 텍스트를 음성으로 변환
            byte[] audioData = textToSpeechService.convertTextToSpeech(translatedResponse);
            if (audioData == null || audioData.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate speech.");
            }

            // 음성 데이터를 Base64로 인코딩
            String audioBase64 = Base64.getEncoder().encodeToString(audioData);


            // 텍스트와 음성 데이터를 JSON 객체로 반환
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("transcribedText", text);
            jsonResponse.put("generatedText", translatedResponse);
            jsonResponse.put("audio", audioBase64);

            return ResponseEntity.ok(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing voice: " + e.getMessage());
        }
    }
}
