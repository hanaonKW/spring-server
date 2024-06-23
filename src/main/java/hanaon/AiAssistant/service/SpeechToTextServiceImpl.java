package hanaon.AiAssistant.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import hanaon.AiAssistant.service.SpeechToTextService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class SpeechToTextServiceImpl implements SpeechToTextService {

    private static final Logger logger = Logger.getLogger(SpeechToTextServiceImpl.class.getName());

    @Value("${gcp-location}")
    private Resource gcpCredentials;

    @Override
    public String convertSpeechToText(MultipartFile file) throws IOException, InterruptedException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file uploaded");
        }

        File tempFile = null;
        try {
            // 임시 파일 생성 및 파일 저장
            tempFile = File.createTempFile("audio", ".wav");
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(tempFile));
            logger.info("Temporary file created: " + tempFile.getAbsolutePath());

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
                        .setSampleRateHertz(16000) // 44100 Hz로 샘플레이트 설정
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
                logger.info("Received recognition response");
                StringBuilder transcription = new StringBuilder();
                for (SpeechRecognitionResult result : response.getResultsList()) {
                    for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
                        transcription.append(alternative.getTranscript());
                    }
                }

                return transcription.toString();
            } catch (Exception e) {
                logger.severe("Exception during transcription: " + e.getMessage());
                throw e;
            } finally {
                speechClient.shutdown();
                speechClient.awaitTermination(30, TimeUnit.SECONDS);
            }

        } catch (Exception e) {
            logger.severe("Error in convertSpeechToText: " + e.getMessage());
            throw e;
        } finally {
            // 임시 파일 삭제
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    logger.warning("Failed to delete temporary file: " + tempFile.getAbsolutePath());
                } else {
                    logger.info("Temporary file deleted: " + tempFile.getAbsolutePath());
                }
            }
        }
    }

}