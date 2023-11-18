package untitle.endproject.demonstration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import untitle.endproject.demonstration.controller.api.WorkVocalController;
import untitle.endproject.demonstration.domain.WorkVocal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VocalUploadController {

    @Autowired
    SessionController sessionController;

    @Autowired
    WorkVocalController workVocalController;

    @PostMapping("/vocal_upload")
    @ResponseBody
    public String uploadFiles(@RequestParam(value="file", required=false) MultipartFile[] files,
                                @RequestParam("userPath") String userPath,
                              @RequestParam("vocalName") String vocalName,
                                    Model model) throws IOException {
        List<String> uploadedFiles = new ArrayList<>();

        // 업로드 경로에 파일 저장
        String vocalPath = "D://WorkSpace/untitled/repository/vocal/" + userPath + "/";

        Path directoryPath = Paths.get(vocalPath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }


        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                // 고유한 파일 이름 생성 (UUID 사용)
//                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                String originalFileName = file.getOriginalFilename();
                File destFile = new File(vocalPath + originalFileName);
                file.transferTo(destFile);

                uploadedFiles.add(destFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println(e);
                uploadedFiles.add("파일 업로드 중 오류가 발생했습니다.");
            }
        }
        WorkVocal wv = new WorkVocal();
        wv.setUuid(sessionController.getUUID().toString());
        wv.setId(sessionController.getMember().getId());
        wv.setVocal_name(vocalName);
        wv.setReady("0");
        System.out.println(vocalName);
        workVocalController.PostWorkVocal(wv);

//        return uploadedFiles;
        model.addAttribute("uploadedFiles", uploadedFiles);
        return "make_vocal_check";
    }
}
