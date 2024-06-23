package hanaon.AiAssistant.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class AudioController {

    @PostMapping("/convert-audio")
    public String convertAudio(@RequestParam("file") MultipartFile file) {
        try {
            // 파일을 임시 디렉터리에 저장
            Path tempFile = Files.createTempFile(null, ".m4a");
            file.transferTo(tempFile);

            // 변환할 WAV 파일 경로
            Path outputPath = Paths.get(tempFile.toAbsolutePath().toString().replace(".m4a", ".wav"));

            // FFmpeg 명령 실행
            ProcessBuilder builder = new ProcessBuilder(
                    "ffmpeg", "-i", tempFile.toString(), "-acodec", "pcm_s16le", "-ar", "16000", outputPath.toString()
            );
            Process process = builder.start();
            process.waitFor();  // 명령 실행이 완료될 때까지 대기

            // 임시 파일 삭제
            Files.delete(tempFile);

            // 결과 반환
            return "Converted audio saved to: " + outputPath.toString();
        } catch (Exception e) {
            return "Error converting audio: " + e.getMessage();
        }
    }
}
