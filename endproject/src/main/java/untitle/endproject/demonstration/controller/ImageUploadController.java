package untitle.endproject.demonstration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageUploadController {

    @Autowired
    SessionController sessionController;
    @PostMapping("/image_upload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("userPath") String userPath,
                             @RequestParam("charName") String charName) {
        if (file.isEmpty()) {
            return "파일을 선택해주세요.";
        }

        try {
            // 고유한 파일 이름 생성 (UUID 사용)
//            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
              sessionController.setTemp(charName);
              sessionController.setModel_info("업로드된 이미지 입니다.");
              sessionController.setPose_num("업로드된 이미지 입니다.");
              sessionController.setImageMessage("업로드된 이미지 입니다.");
              sessionController.setModel_info_text("업로드된 이미지 입니다.");
              sessionController.setPose_num_text("업로드된 이미지 입니다.");



            // 업로드 경로에 파일 저장
            String imagePath = "D://WorkSpace/untitled/repository/image/" + userPath + "/";

            Path directoryPath = Paths.get(imagePath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            File destFile = new File(imagePath + userPath + ".png");
            file.transferTo(destFile);

            String returnForm =
                    """
                        <div id=\"imageOutputZone\">
                            <img src = 
                    """
                    +
                            "\"/apiResult/image/" + userPath  + "/" + userPath + ".png\""
//                            destFile.getAbsolutePath()
                    +
                    """
                        /></div>     
                    """;

            // 저장된 파일의 경로 반환
//            return destFile.getAbsolutePath();
            return returnForm;

        } catch (IOException e) {
            System.out.println(e);
            return "파일 업로드 중 오류가 발생했습니다.";
        }
    }
}
