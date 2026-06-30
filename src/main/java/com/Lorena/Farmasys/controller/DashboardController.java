package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.service.FarmaceuticoService;
import com.Lorena.Farmasys.service.VendaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final FarmaceuticoService service = new FarmaceuticoService();
    private final VendaService vendaService = new VendaService();

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        model.addAttribute("lista", service.listar());
        model.addAttribute("vendasRealizadas", vendaService.listarComDetalhes());
        return "dashboard";
    }
}
