package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.TextToSpeechService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/text-to-speech")
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService textToSpeechService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertTextToSpeech(@RequestBody String requestBody) {
        try {
            // JSON 요청 본문에서 텍스트 추출
            JSONObject json = new JSONObject(requestBody);
            String text = json.getString("text");

            // 텍스트를 음성으로 변환
            byte[] audioData = textToSpeechService.convertTextToSpeech(text);
            if (audioData == null || audioData.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate speech.".getBytes());
            }

            // 음성 데이터를 반환
            return ResponseEntity.ok().header("Content-Type", "audio/mpeg").body(audioData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Error processing text: " + e.getMessage()).getBytes());
        }
    }
}
