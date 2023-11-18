package untitle.endproject.demonstration.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }
}
