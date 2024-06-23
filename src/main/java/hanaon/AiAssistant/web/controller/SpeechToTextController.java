package hanaon.AiAssistant.web.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/speech")
public class SpeechToTextController {

    private static final Logger logger = Logger.getLogger(SpeechToTextController.class.getName());

    @Value("${gcp-location}")
    private Resource gcpCredentials;

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribe(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }

        File tempFile = null;
        try {
            // 임시 파일 생성 및 파일 저장
            tempFile = File.createTempFile("audio", ".wav");
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(tempFile));

            // Google Cloud 인증 설정
            GoogleCredentials credentials = GoogleCredentials.fromStream(gcpCredentials.getInputStream());
            credentials = credentials.createScoped("https://www.googleapis.com/auth/cloud-platform");
            SpeechSettings speechSettings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            SpeechClient speechClient = SpeechClient.create(speechSettings);

            // Google Speech-to-Text 클라이언트 생성
            try {
                RecognitionConfig config = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setSampleRateHertz(16000)
                        .setLanguageCode("ko-KR")
                        .build();

                RecognitionAudio audio = RecognitionAudio.newBuilder()
                        .setContent(com.google.protobuf.ByteString.readFrom(new FileInputStream(tempFile)))
                        .build();

                RecognizeRequest request = RecognizeRequest.newBuilder()
                        .setConfig(config)
                        .setAudio(audio)
                        .build();

                RecognizeResponse response = speechClient.recognize(request);
                StringBuilder transcription = new StringBuilder();
                for (SpeechRecognitionResult result : response.getResultsList()) {
                    for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
                        transcription.append(alternative.getTranscript());
                    }
                }

                return ResponseEntity.ok(transcription.toString());
            } finally {
                speechClient.shutdown();
                speechClient.awaitTermination(30, TimeUnit.SECONDS);
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("IOException: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Failed to transcribe audio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            // 임시 파일 삭제
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    logger.warning("Failed to delete temporary file: " + tempFile.getAbsolutePath());
                }
            }
        }
    }
}
