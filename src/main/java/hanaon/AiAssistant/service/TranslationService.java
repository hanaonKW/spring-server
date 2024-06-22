package hanaon.AiAssistant.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    @Value("${deepl.api.key}")
    private String apiKey;

    @Autowired
    private TextGenerationService textGenerationService;  // 텍스트 생성 서비스 주입

    public String translateText(String text, String sourceLang, String targetLang) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api-free.deepl.com/v2/translate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8")));
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

        String body = "text=" + text + "&source_lang=" + sourceLang + "&target_lang=" + targetLang;
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // JSON 파싱하여 translated text 추출
        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray translations = jsonResponse.getJSONArray("translations");
        return translations.getJSONObject(0).getString("text");
//        return response.getBody();
    }

    public String processRequest(String prompt) throws Exception {
        // 한국어 입력을 영어로 번역
        String translatedToEnglish = translateText(prompt, "KO", "EN");
        System.out.println("translatedToEnglish = " + translatedToEnglish);

        // 영어로 GPT-3 응답 생성
        String generatedText = textGenerationService.generateText(translatedToEnglish);
        System.out.println("Generated text: " + generatedText);

        // 생성된 응답 리턴
        return generatedText;
//        return translateText(generatedText, "EN", "KO");
    }
}