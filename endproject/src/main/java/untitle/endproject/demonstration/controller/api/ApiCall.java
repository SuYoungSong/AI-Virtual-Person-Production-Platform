package untitle.endproject.demonstration.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import untitle.endproject.demonstration.controller.SessionController;
import untitle.endproject.demonstration.domain.*;

import java.util.*;

@Controller
@RequestMapping("/api")
public class ApiCall {

    @Autowired
    SessionController sessionController;
    @Autowired
    WorkCharController workCharController;

    @PostMapping("/image")
    public String imageCall(Model model, Image image){
         Map<String, String> params = new HashMap<>();

        sessionController.setTemp(image.getCharName());
        sessionController.setImageMessage(image.getPrompt());

        sessionController.setModel_info(image.getModelName());
        sessionController.setModel_info_text(image.getModelNameText());

        sessionController.setPose_num(image.getPoseImagePath());
        sessionController.setPose_num_text(image.getPoseImagePathText());


        params.put("model_version", image.getModelVersion());
        params.put("user_path", image.getUserPath());
        params.put("prompt_user", image.getPrompt());
        params.put("negative_prompt_user", image.getNegativePrompt());
        params.put("model_name", image.getModelName());
        params.put("pose_image_path", "pose_to_image/" + image.getPoseImagePath());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_image/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String imagePath = response.getBody().replaceAll("\"","");
        model.addAttribute("imagePath","/apiResult/image/"+ image.getUserPath() + "/" + imagePath);

        return "imageResult";
    }

    @PostMapping("/audio")
    public String audioCall(Model model, Audio audio){

        Map<String, String> params = new HashMap<>();

        sessionController.setAudioMessage(audio.getText());
        sessionController.setAudio_model_info(audio.getModel_name());
        sessionController.setAudio_model_text(audio.getModel_name_text());

        params.put("user_path", audio.getUserPath());
        params.put("text", audio.getText());
        params.put("model_name", audio.getModel_name());


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_voice/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String audioPath = response.getBody().replaceAll("\"","");
        audioPath = audioPath.replace(".wav","");
        Random random = new Random();
        int specificRange = random.nextInt(999999);
        int specificRange2 = random.nextInt(999999);

        model.addAttribute("audioPath","/apiResult/audio/"+ audio.getUserPath() + "/" + audioPath + ".wav?" + specificRange + "=" + specificRange2 );
        return "audioResult";
    }

    @PostMapping("/audio_vits")
    public String audioCallVits(Model model, Audio audio){

        Map<String, String> params = new HashMap<>();

        params.put("user_path", audio.getUserPath());
        params.put("text", audio.getText());
        params.put("model_name", audio.getModel_name());

        sessionController.setAudioMessage(audio.getText());
        sessionController.setAudio_model_info(audio.getModel_name());
        sessionController.setAudio_model_text(audio.getModel_name_text());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_voice_vits/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String audioPath = response.getBody().replaceAll("\"","");
        audioPath = audioPath.replace(".wav","");
        Random random = new Random();
        int specificRange = random.nextInt(999999);
        int specificRange2 = random.nextInt(999999);

        model.addAttribute("audioPath","/apiResult/audio/"+ audio.getUserPath() + "/" + audioPath + ".wav?" + specificRange + "=" + specificRange2 );

        return "audioResult";
    }

    @PostMapping("/audio_vc")
    public String audioCallVC(Model model, Audio audio){

        Map<String, String> params = new HashMap<>();

        List<String> vocal_model_not_men = new ArrayList<>();
        vocal_model_not_men.add("anna-asti");
        vocal_model_not_men.add("IU");
        vocal_model_not_men.add("talkingtom2012");
        vocal_model_not_men.add("Leonid");
        vocal_model_not_men.add("MaiDavika350");
        vocal_model_not_men.add("billieeilishlive");

//        params.put("user_path", audio.getUserPath());
//        params.put("text", audio.getText());
//        params.put("model_name", "kss");

        String speaker = "3";

        if ( vocal_model_not_men.contains(audio.getModel_name())){
            speaker = "5061";
        }



        params.put("speaker", speaker);
        params.put("prompt", audio.getText());
        params.put("uuid", audio.getUserPath());


        sessionController.setAudioMessage(audio.getText());
        sessionController.setAudio_model_info(audio.getModel_name());
        sessionController.setAudio_model_text(audio.getModel_name_text());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

//        ResponseEntity<String> response = rt.exchange(
//                "http://localhost:8000/api/make_voice_vits/",
//                HttpMethod.POST,
//                entity,
//                String.class
//        );
//        String audioPath = response.getBody().replaceAll("\"","");

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_voice_kt/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String audioPath = response.getBody().replaceAll("\"","");



        // 파일 경로
        String base_path = "D://WorkSpace/untitled/repository/audio/" + audio.getUserPath() + "/" + audioPath;

        // vc 처리하기
        params.clear();

        params.put("f0_key", "0");
        params.put("wav_path", base_path);
        params.put("model_name", audio.getModel_name());

        response = rt.exchange(
                "http://localhost:8000/api/make_voice_vc/",
                HttpMethod.POST,
                entity,
                String.class
        );
        audioPath = audioPath.replace(".wav","");
        Random random = new Random();
        int specificRange = random.nextInt(999999);
        int specificRange2 = random.nextInt(999999);

        model.addAttribute("audioPath","/apiResult/audio/"+ audio.getUserPath() + "/" + audioPath + ".wav?" + specificRange + "=" + specificRange2 );
        return "audioResult";
    }

    @PostMapping("/create_video_reposi")
    public String create_video_reposi(@RequestParam String userPath,
                                      @RequestParam String text,
                                      @RequestParam String voice_model,
                                      Model model){
        String[] tacotronTTS = { "chungcheong", "gangwon", "gyeongsang", "jeju", "jeolla" };
        List<String> tacotronList = Arrays.asList(tacotronTTS);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        Map<String, String> params = new HashMap<>();
        RestTemplate rt = new RestTemplate();
        String url = "";

        String imagePath = "repository/image/" + userPath + "/" + userPath + ".png";
        String audioPath = "repository/video_reposi/" + userPath + "/" + userPath + ".wav";

        // 음성 만들기
        // tacotron2인 경우
        if (tacotronList.contains(voice_model)) {
            url = "http://localhost:8000/api/make_voice/";
            params.put("user_path", userPath);
            params.put("text", text);
            params.put("model_name", voice_model);
            params.put("is_video_reposi", "1");

        } else if (voice_model.equals("kss")){
        // vits인 경우
            url = "http://localhost:8000/api/make_voice_vits/";
            params.put("user_path", userPath);
            params.put("text", text);
            params.put("model_name", voice_model);
            params.put("is_video_reposi", "1");
        }else{
        // rvc인 경우
            List<String> vocal_model_not_men = new ArrayList<>();
            vocal_model_not_men.add("anna-asti");
            vocal_model_not_men.add("IU");
            vocal_model_not_men.add("talkingtom2012");
            vocal_model_not_men.add("Leonid");
            vocal_model_not_men.add("MaiDavika350");
            vocal_model_not_men.add("billieeilishlive");

            String speaker = "3";
            if ( vocal_model_not_men.contains(voice_model)){
                speaker = "5061";
            }

            url = "http://localhost:8000/api/make_voice_kt/";
            params.put("speaker", speaker);
            params.put("prompt",text);
            params.put("uuid", userPath);
            params.put("is_video_reposi", "1");

            HttpEntity<Map<String, String>> entity2 = new HttpEntity<>(params, headers);
            ResponseEntity<String> response2 = rt.exchange(
                    url,
                    HttpMethod.POST,
                    entity2,
                    String.class
            );


            // vc 처리하기
            params.clear();
            url = "http://localhost:8000/api/make_voice_vc/";
            params.put("f0_key", "0");
            params.put("wav_path", audioPath);
            params.put("model_name", voice_model);


        }
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        // 비디오 만들기
        params.clear();
        params.put("user_path", userPath);
        params.put("image_path", imagePath);
        params.put("audio_path", audioPath);
        params.put("is_video_reposi", "1");

        entity = new HttpEntity<>(params, headers);
        rt = new RestTemplate();

        response = rt.exchange(
                "http://localhost:8000/api/make_video/",
                HttpMethod.POST,
                entity,
                String.class
        );

        Random random = new Random();
        int specificRange = random.nextInt(999999);
        int specificRange2 = random.nextInt(999999);

        String videoPath = response.getBody().replaceAll("\"","");
        model.addAttribute("videoPath","/apiResult/video_reposi/"+userPath + "/" + userPath + videoPath + "?" + specificRange + "=" + specificRange2);
        model.addAttribute("videoClass",userPath);

        return "videoResult2";

    }

    @PostMapping("/audio_vc_reposi")
    public String audioCallVC_reposi(Model model, Audio audio){

        Map<String, String> params = new HashMap<>();

        List<String> vocal_model_not_men = new ArrayList<>();
        vocal_model_not_men.add("anna-asti");
        vocal_model_not_men.add("IU");
        vocal_model_not_men.add("talkingtom2012");
        vocal_model_not_men.add("Leonid");
        vocal_model_not_men.add("MaiDavika350");
        vocal_model_not_men.add("billieeilishlive");

        String speaker = "3";

        if ( vocal_model_not_men.contains(audio.getModel_name())){
            speaker = "5061";
        }


        params.put("speaker", speaker);
        params.put("prompt", audio.getText());
        params.put("uuid", audio.getModel_name());
        params.put("is_reposi", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_voice_kt/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String audioPath = response.getBody().replaceAll("\"","");

        // 파일 경로
        String base_path = "D://WorkSpace/untitled/repository/audio_reposi/" + audio.getModel_name() + "/" + audioPath;

        // vc 처리하기
        params.clear();

        params.put("f0_key", "0");
        params.put("wav_path", base_path);
        params.put("model_name", audio.getModel_name());

        response = rt.exchange(
                "http://localhost:8000/api/make_voice_vc/",
                HttpMethod.POST,
                entity,
                String.class
        );
        audioPath = audioPath.replace(".wav","");
        Random random = new Random();
        int specificRange = random.nextInt(999999);
        int specificRange2 = random.nextInt(999999);

        model.addAttribute("audioPath","/apiResult/audio_reposi/"+ audio.getModel_name() + "/" + audioPath + ".wav?" + specificRange + "=" + specificRange2 );
        model.addAttribute("audioClass", audio.getUserPath());
        return "audioResult2";
    }

    @PostMapping("/video")
    public String videoCall(Model model, Video video){
        Map<String, String> params = new HashMap<>();

        params.put("user_path", video.getUserPath());
        params.put("image_path", video.getImagePath());
        params.put("audio_path", video.getAudioPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.add("Connection","keep-alive");
        headers.add("Accept","*/*");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        System.out.println(entity.getBody());
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://localhost:8000/api/make_video/",
                HttpMethod.POST,
                entity,
                String.class
        );
        String videoPath = response.getBody().replaceAll("\"","");
        model.addAttribute("videoPath","/apiResult/video/"+ video.getUserPath() + "/" + videoPath);


        String imageMessage = sessionController.getImageMessage();

        if (imageMessage == null){
            imageMessage = "업로드된 이미지 입니다.";
        }

        String audioMessage = sessionController.getAudioMessage();

        if (audioMessage == null){
            audioMessage = "업로드된 목소리 입니다.";
        }

        WorkChar wc = new WorkChar();
        wc.setUuid(sessionController.getUUID().toString());
        wc.setImage_prompt(imageMessage);
        wc.setVoice_prompt(audioMessage);
        wc.setId(sessionController.getMember().getId());
        wc.setChar_name(sessionController.getTemp());

        wc.setModel_info(sessionController.getModel_info());
        wc.setAudio_model_info(sessionController.getAudio_model_info());
        wc.setPose_num(sessionController.getPose_num());

        wc.setPose_num_text(sessionController.getPose_num_text());
        wc.setModel_info_text(sessionController.getModel_info_text());
        wc.setAudio_model_text(sessionController.getAudio_model_text());


        workCharController.PostWorkChar(wc);

        return "videoResult";
    }
}
