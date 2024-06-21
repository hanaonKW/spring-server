package hanaon.AiAssistant;

import hanaon.AiAssistant.service.TextGenerationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public TextGenerationService textGenerationService() {
        return new TextGenerationService();
    }
}