package hanaon.AiAssistant.service;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TextGenerationService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.fine_tuned_model}")
    private String fineTunedModel;

    public String generateText(String prompt) throws Exception {
        System.out.println("API URL: " + apiUrl);
        System.out.println("API Key: " + apiKey);
        System.out.println("Using fine-tuned model: " + fineTunedModel);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(apiUrl + "/chat/completions");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + apiKey);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", fineTunedModel); // 파인 튜닝된 모델 ID 사용

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "You are a knowledgeable assistant who provides detailed and accurate information in Korean."));
            messages.put(new JSONObject().put("role", "user").put("content", prompt));
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 150);

            StringEntity entity = new StringEntity(requestBody.toString());
            post.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseString = EntityUtils.toString(response.getEntity());
                System.out.println("Response JSON: " + responseString);  // 디버깅을 위한 출력
                JSONObject jsonResponse = new JSONObject(responseString);
                return jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }
        }
    }
}