package untitle.endproject.demonstration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class myPageControll {


    @GetMapping()
    public String mainPage(){ return  "mypage"; }

//    @GetMapping("/char")
//    public String makeChar(){ return  "make_char_image"; }


}

