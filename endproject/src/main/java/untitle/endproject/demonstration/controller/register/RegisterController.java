package untitle.endproject.demonstration.controller.register;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    @GetMapping("/registerPage")
    public String register() {
        return "register";
    }
}
