package hanaon.AiAssistant.service;

import hanaon.AiAssistant.TestConfig;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = TestConfig.class)
class TextGenerationServiceTest {

    @MockBean
    private CloseableHttpClient httpClient;

    @Autowired
    private TextGenerationService textGenerationService;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateText() throws Exception {
        System.out.println("TEST API URL: " + apiUrl);
        System.out.println("TEST API Key: " + apiKey);

        String prompt = "Hello, how are you?";
        String expectedResponse = "{\"choices\":[{\"message\":{\"content\":\"I'm fine, thank you!\"}}]}";

        // Mock the HTTP response
        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        HttpEntity entity = new StringEntity(expectedResponse);

        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);

        String response = textGenerationService.generateText(prompt);

        assertThat(response).isEqualTo("I'm fine, thank you!");

        // Verify that the HTTP client was called
        verify(httpClient, times(1)).execute(any(HttpPost.class));

        // Close the mocked response to avoid resource leak
        httpResponse.close();
    }
}