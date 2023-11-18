package untitle.endproject.demonstration.controller.logout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import untitle.endproject.demonstration.controller.SessionController;

@Controller
public class LogoutController {

    @Autowired
    private SessionController sessionController;
    @GetMapping("/logout")
    public String logout() {
        sessionController.setMember(null);
        return "index";
    }
}
