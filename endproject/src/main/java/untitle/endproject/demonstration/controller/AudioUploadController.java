package untitle.endproject.demonstration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AudioUploadController {

    @Autowired
    SessionController sessionController;
    @PostMapping("/audio_upload")
    @ResponseBody
    public String uploadAudioFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("userPath") String userPath,
                             @RequestParam("modelName") String modelName,
                             @RequestParam("f0_key") String f0_key,
                             Model model) {
        if (file.isEmpty()) {
            return "파일을 선택해주세요.";
        }
        try {
            // 고유한 파일 이름 생성 (UUID 사용)
//            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            sessionController.setAudio_model_info(modelName);
            sessionController.setAudioMessage("업로드된 오디오 입니다.");
            // 업로드 경로에 파일 저장
            String audioPath = "D://WorkSpace/untitled/repository/audio/" + userPath + "/";

            Path directoryPath = Paths.get(audioPath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            File destFile = new File(audioPath + userPath + ".wav");
            file.transferTo(destFile);

//          1. 음성파일 보컬 추출하기
            Map<String, String> params = new HashMap<>();
            params.put("uuid", userPath);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type","application/json");
            headers.add("Connection","keep-alive");
            headers.add("Accept","*/*");

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
            System.out.println(entity.getBody());
            RestTemplate rt = new RestTemplate();

            ResponseEntity<String> response = rt.exchange(
                    "http://localhost:8000/api/uvr/",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

//          2. VC 처리하기
            params.clear();
            params.put("f0_key", f0_key);
            params.put("wav_path", audioPath + userPath + ".wav");
            params.put("model_name", modelName);


            entity = new HttpEntity<>(params, headers);
            System.out.println(entity.getBody());
            rt = new RestTemplate();

            response = rt.exchange(
                    "http://localhost:8000/api/make_voice_vc/",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
//          3. 반환하기
            audioPath = audioPath.replace(".wav","");
            model.addAttribute("audioPath","/apiResult/audio/"+userPath + "/" + audioPath + ".wav");

            return "audioResult";

        } catch (IOException e) {
            System.out.println(e);
            return "파일 업로드 중 오류가 발생했습니다.";
        }
    }
}
