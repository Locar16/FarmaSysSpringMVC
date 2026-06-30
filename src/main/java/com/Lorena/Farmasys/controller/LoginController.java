package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Farmaceutico;
import com.Lorena.Farmasys.service.FarmaceuticoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final FarmaceuticoService service = new FarmaceuticoService();

    // Tela de login: index.jsp foi movido para WEB-INF/pages/ e passa a ser
    // servido por este controlador na raiz da aplicacao.
    @GetMapping("/")
    public String exibirLogin() {
        return "index";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String usuario,
                             @RequestParam String senha,
                             HttpSession session,
                             RedirectAttributes redirectAttrs) {
        Farmaceutico f = service.autenticar(usuario, senha);
        if (f != null) {
            session.setAttribute("farmaceutico", f);
            return "redirect:/dashboard";
        } else {
            redirectAttrs.addAttribute("msg", "Usuário ou senha inválidos.");
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }
}
