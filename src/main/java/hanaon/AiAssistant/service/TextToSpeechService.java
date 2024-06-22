package hanaon.AiAssistant.service;

public interface TextToSpeechService {
    byte[] convertTextToSpeech(String text) throws Exception;
}
