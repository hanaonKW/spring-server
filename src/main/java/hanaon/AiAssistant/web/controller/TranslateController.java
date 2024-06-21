package hanaon.AiAssistant.web.controller;

import hanaon.AiAssistant.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class TranslateController {

    @Autowired
    private TranslationService translationService;

    // POST 메서드를 사용하여 텍스트 생성 요청을 처리합니다.
    @PostMapping
    public String translateText(@RequestParam("text") String text) {
        try {
            // TranslationService의 processRequest 메서드 호출
            return translationService.processRequest(text);
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            return "Error during translation: " + e.getMessage();
        }
    }
}