package hanaon.AiAssistant.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import hanaon.AiAssistant.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class TextToSpeechServiceImpl implements TextToSpeechService {

    private static final Logger logger = Logger.getLogger(TextToSpeechServiceImpl.class.getName());

    @Value("${google.cloud.credentials.location}")
    private Resource gcpCredentials;

    @Override
    public byte[] convertTextToSpeech(String text) throws Exception {
        // Google Cloud 인증 설정
        GoogleCredentials credentials = GoogleCredentials.fromStream(gcpCredentials.getInputStream());
        credentials = credentials.createScoped("https://www.googleapis.com/auth/cloud-platform");
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings);

        // Google Text-to-Speech 클라이언트 생성
        try {
            // 요청 구성
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ko-KR") // 한국어
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3) // MP3 포맷
                    .build();

            // 요청 실행
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // 응답 처리
            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();
        } finally {
            textToSpeechClient.shutdown();
            textToSpeechClient.awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}
