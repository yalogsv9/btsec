package vn.edu.ltweb.springws.vidu2;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.ltweb.springws.vidu2.security.CustomUserDetails;

@Controller
public class Vidu2Controller {
    @GetMapping("/vidu2/login") String login() { return "vidu2/login"; }
    @GetMapping("/vidu2") String home(Authentication authentication, Model model) {
        model.addAttribute("user", (CustomUserDetails) authentication.getPrincipal());
        return "vidu2/home";
    }
    @GetMapping("/vidu2/admin/panel") String admin() { return "vidu2/admin"; }
}
