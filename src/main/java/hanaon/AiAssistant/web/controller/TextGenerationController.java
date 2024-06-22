package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.TextGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/text")
public class TextGenerationController {

    @Autowired
    private TextGenerationService textGenerationService;

    @PostMapping("/generate")
    public String generateText(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        System.out.println("Received prompt: " + prompt);  // 로그 메시지 추가
        try {
            String result = textGenerationService.generateText(prompt);
            System.out.println("Generated text: " + result);  // 로그 메시지 추가
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating text";
        }
    }
}