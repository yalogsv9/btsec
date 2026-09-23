package vn.edu.ltweb.springws.vidu3;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Vidu3Controller {

    @GetMapping("/vidu3/login")
    String login() {
        return "vidu3/login";
    }

    @GetMapping("/vidu3")
    String home(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "vidu3/home";
    }
}
