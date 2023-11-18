package untitle.endproject.demonstration.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import untitle.endproject.demonstration.controller.MemberController;
import untitle.endproject.demonstration.controller.SessionController;
import untitle.endproject.demonstration.domain.Member;
import untitle.endproject.demonstration.repository.MemberRepository;

@Controller
public class LoginProcessController {

    @Autowired
    private MemberController memberController;

    @Autowired
    private SessionController sessionController;

    @GetMapping("/login")
    public String getLogin(){
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String pw, Model model) {
        model.addAttribute("loginId", id);

        if (sessionController.getMember() != null){
            return "index";
        }

        try {
            Member member = memberController.GetUser(id);
            if (member.checkPassword(pw)){

                sessionController.setMember(member);
                return "index";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage","아이디 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }
        model.addAttribute("errorMessage","아이디 또는 비밀번호가 잘못되었습니다.");
        return "login";
    }




}
