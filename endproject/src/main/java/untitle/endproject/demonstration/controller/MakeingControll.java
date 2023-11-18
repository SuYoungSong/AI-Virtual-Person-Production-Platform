package untitle.endproject.demonstration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import untitle.endproject.demonstration.controller.api.WorkCharController;
import untitle.endproject.demonstration.controller.api.WorkVocalController;
import untitle.endproject.demonstration.domain.WorkChar;
import untitle.endproject.demonstration.domain.WorkVocal;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/workspace")
public class MakeingControll {

    @Autowired
    SessionController sessionController;
    @Autowired
    WorkCharController workCharController;
    @Autowired
    WorkVocalController workVocalController;

    @GetMapping()
    public String mainPage(){
        return  "workspace";
    }

    @GetMapping("/char")
    public String makeChar(){
        if (sessionController.getMember() == null){
            return "login";
        }
        sessionController.setUUID(UUID.randomUUID());
        sessionController.setTemp(null);
        sessionController.setImageMessage(null);
        sessionController.setAudioMessage(null);
        return  "make_char_image";
    }

    @GetMapping("/char_voice")
    public String makeCharVoice(Model model){
        if (sessionController.getMember() == null){
            return "login";
        }

        if (sessionController.getUUID() == null){
            return "workspace";
        }
        List<WorkVocal> wv = workVocalController.GetWorkCharsById(sessionController.getMember().getId());

        if ( wv.isEmpty() ) wv = null;

        model.addAttribute("vocal",wv);

        return  "make_char_voice";
    }

    @GetMapping("/char_media")
    public String makeCharMedia(){
        if (sessionController.getMember() == null){
            return "login";
        }

        if (sessionController.getUUID() == null){
            return "workspace";
        }

        return  "make_char_media";
    }


    @GetMapping("/vocal")
    public String makeVocal(){
        if (sessionController.getMember() == null){
            return "login";
        }
        sessionController.setUUID(UUID.randomUUID());
        sessionController.setTemp(null);
        return  "make_vocal_upload";
    }

    @GetMapping("/vocal_check")
    public String makeVocalCheck(){
        if (sessionController.getMember() == null){
            return "login";
        }

        if (sessionController.getUUID() == null){
            return "workspace";
        }

        return  "make_vocal_check";
    }

    @GetMapping("/vocal_ready")
    public String makeVocalReady(){
        if (sessionController.getMember() == null){
            return "login";
        }

        if (sessionController.getUUID() == null){
            return "workspace";
        }
        return  "make_vocal_ready";
    }


    @GetMapping("/reposi")
    public String reposi(Model model){
        if (sessionController.getMember() == null){
            return "login";
        }

        List<WorkChar> wc = workCharController.GetWorkCharsById(sessionController.getMember().getId());
        List<WorkVocal> wv = workVocalController.GetWorkCharsById(sessionController.getMember().getId());

        if ( wc.isEmpty() ) wc = null;
        if ( wv.isEmpty() ) wv = null;

        model.addAttribute("char",wc);
        model.addAttribute("vocal",wv);

        return  "reposi";
    }

}
