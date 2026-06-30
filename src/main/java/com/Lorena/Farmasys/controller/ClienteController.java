package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Cliente;
import com.Lorena.Farmasys.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService service = new ClienteService();

    @GetMapping
    public String listar(@RequestParam(required = false) String acao,
                         @RequestParam(required = false) Integer id,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttrs) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        if ("editar".equals(acao) && id != null) {
            model.addAttribute("cliente", service.buscarPorId(id));

        } else if ("excluir".equals(acao) && id != null) {
            try {
                service.excluir(id);
                redirectAttrs.addAttribute("msg", "Cliente excluído com sucesso.");
            } catch (Exception e) {
                redirectAttrs.addAttribute("erro", e.getMessage());
            }
            return "redirect:/cliente";
        }

        model.addAttribute("lista", service.listar());
        return "clientes";
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam String cpf,
                         @RequestParam(required = false) String telefone,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String endereco,
                         @RequestParam(name = "data_nascimento", required = false) String dataNascimento,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttrs) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        boolean isUpdate = id != null;

        Cliente c = new Cliente();
        if (isUpdate) c.setId(id);

        c.setNome(nome);
        c.setCpf(cpf);
        c.setTelefone(telefone);
        c.setEmail(email);
        c.setEndereco(endereco);

        if (dataNascimento != null && !dataNascimento.isBlank())
            c.setDataNascimento(LocalDate.parse(dataNascimento));

        try {
            String msg;
            if (isUpdate) {
                service.atualizar(c);
                msg = "Cliente atualizado com sucesso.";
            } else {
                service.inserir(c);
                msg = "Cliente cadastrado com sucesso.";
            }
            redirectAttrs.addAttribute("msg", msg);
            return "redirect:/cliente";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("cliente", c);
            model.addAttribute("lista", service.listar());
            return "clientes";
        }
    }
}
