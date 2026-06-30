package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Farmaceutico;
import com.Lorena.Farmasys.service.FarmaceuticoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/farmaceutico")
public class FarmaceuticoController {

    private static final List<String> UFS = List.of(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    );

    private final FarmaceuticoService service = new FarmaceuticoService();

    @GetMapping
    public String listar(@RequestParam(required = false) String acao,
                         @RequestParam(required = false) Integer id,
                         HttpSession session,
                         Model model) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");

        if ("editar".equals(acao) && id != null) {
            if (logado.getId() != id) {
                return "redirect:/farmaceutico";
            }
            model.addAttribute("farmaceuticoEdicao", service.buscarPorId(id));
        }

        model.addAttribute("lista", service.listarAtivos());
        model.addAttribute("ufs", UFS);
        return "farmaceutico";
    }

    @PostMapping
    public String salvar(@RequestParam int id,
                         @RequestParam(required = false) String acao,
                         @RequestParam(required = false) String usuario,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String cpf,
                         @RequestParam(name = "crf_numero", required = false) String crfNumero,
                         @RequestParam(name = "crf_uf", required = false) String crfUf,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String telefone,
                         @RequestParam(required = false) String salario,
                         @RequestParam(name = "data_admissao", required = false) String dataAdmissao,
                         @RequestParam(required = false) String senhaAtual,
                         @RequestParam(required = false) String novaSenha,
                         @RequestParam(required = false) String novaSenha2,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttrs) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");

        if (logado.getId() != id) {
            return "redirect:/farmaceutico";
        }

        if ("desativarConta".equals(acao)) {
            service.desativar(id);
            session.invalidate();
            return "redirect:/";
        }

        if (!logado.getSenha().equals(senhaAtual)) {
            model.addAttribute("erroSenha", "Senha atual incorreta.");
            return reexibirEdicao(model, logado.getId());
        }

        String senhaParaSalvar;
        if (novaSenha != null && !novaSenha.isBlank()) {
            if (!novaSenha.equals(novaSenha2)) {
                model.addAttribute("erroSenha", "As senhas não conferem.");
                return reexibirEdicao(model, logado.getId());
            }
            senhaParaSalvar = novaSenha;
        } else {
            senhaParaSalvar = logado.getSenha();
        }

        Farmaceutico f = new Farmaceutico();
        f.setId(id);
        f.setUsuario(usuario);
        f.setNome(nome);
        f.setCpf(cpf);
        f.setCrfNumero(crfNumero);
        f.setCrfUf(crfUf);
        f.setEmail(email);
        f.setTelefone(telefone);
        f.setSenha(senhaParaSalvar);
        f.setAtivo(logado.isAtivo());

        if (salario != null && !salario.isBlank())
            f.setSalario(new BigDecimal(salario.replace(",", ".")));

        if (dataAdmissao != null && !dataAdmissao.isBlank())
            f.setDataAdmissao(LocalDate.parse(dataAdmissao));

        try {
            service.atualizar(f);
            session.setAttribute("farmaceutico", service.buscarPorId(id));
            redirectAttrs.addAttribute("msg", "editado");
            return "redirect:/farmaceutico";
        } catch (Exception e) {
            model.addAttribute("erroSenha", e.getMessage());
            return reexibirEdicao(model, logado.getId());
        }
    }

    private String reexibirEdicao(Model model, int id) {
        model.addAttribute("farmaceuticoEdicao", service.buscarPorId(id));
        model.addAttribute("lista", service.listarAtivos());
        model.addAttribute("ufs", UFS);
        return "farmaceutico";
    }
}
