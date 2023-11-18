package untitle.endproject.demonstration.controller.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import untitle.endproject.demonstration.controller.MemberController;
import untitle.endproject.demonstration.controller.SessionController;
import untitle.endproject.demonstration.domain.Member;

@Controller
public class RegisterProcessController {
    @Autowired
    private MemberController memberController;

    @Autowired
    private SessionController sessionController;

    @GetMapping("/register")
    public String getRegister(){
        return "index";
    }

    @PostMapping("/register")
    public String login(@RequestParam String id, @RequestParam String pw, @RequestParam String phone, @RequestParam String nickname, @RequestParam String userName, Model model) {

        if (sessionController.getMember() != null){
            return "index";
        }

        Member member = new Member();
        member.setId(id);
        member.setPassword(pw);
        member.setPhone(phone);
        member.setNickname(nickname);
        member.setUserName(userName);

        if (memberController.GetUser(id) == null){
                memberController.PostUser(member);
                sessionController.setMember(member);

                return "index";
        }else{
            model.addAttribute("memberInfo", member);
            model.addAttribute("errorMessage", "이미 사용중인 아이디 입니다.");
            return "register";
        }

    }
}

