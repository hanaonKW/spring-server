package hanaon.AiAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"hanaon.AiAssistant.service", "hanaon.AiAssistant.web.controller"})
public class AiAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAssistantApplication.class, args);
	}

}
