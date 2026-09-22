package vn.edu.ltweb.springws.vidu1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class Vidu1Controller {

    @GetMapping("/vidu1")
    String home() {
        return "vidu1/home";
    }

    @GetMapping("/vidu1/private")
    String privatePage(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "vidu1/private";
    }

    @GetMapping("/login")
    String login() {
        return "vidu1/login";
    }
}
