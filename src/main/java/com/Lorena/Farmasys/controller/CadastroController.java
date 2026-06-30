package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Farmaceutico;
import com.Lorena.Farmasys.service.FarmaceuticoService;
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
@RequestMapping("/cadastro")
public class CadastroController {

    private static final List<String> UFS = List.of(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    );

    private final FarmaceuticoService service = new FarmaceuticoService();

    @GetMapping
    public String exibirCadastro(Model model) {
        model.addAttribute("ufs", UFS);
        return "cadastro";
    }

    @PostMapping
    public String cadastrar(@RequestParam String usuario,
                            @RequestParam String nome,
                            @RequestParam String cpf,
                            @RequestParam(name = "crf_numero") String crfNumero,
                            @RequestParam(name = "crf_uf") String crfUf,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String telefone,
                            @RequestParam(required = false) String senha,
                            @RequestParam(required = false) String senha2,
                            @RequestParam(required = false) String salario,
                            @RequestParam(name = "data_admissao", required = false) String dataAdmissao,
                            Model model,
                            RedirectAttributes redirectAttrs) {

        Farmaceutico f = new Farmaceutico();
        f.setUsuario(usuario);
        f.setNome(nome);
        f.setCpf(cpf);
        f.setCrfNumero(crfNumero);
        f.setCrfUf(crfUf);
        f.setEmail(email);
        f.setTelefone(telefone);
        f.setSenha(senha);

        if (salario != null && !salario.isBlank())
            f.setSalario(new BigDecimal(salario.replace(",", ".")));

        if (dataAdmissao != null && !dataAdmissao.isBlank())
            f.setDataAdmissao(LocalDate.parse(dataAdmissao));

        // Confirmação de senha validada no servidor antes de qualquer cadastro.
        if (senha == null || senha.isBlank() || senha2 == null || senha2.isBlank()) {
            return devolverComErro(model, f, "Informe a senha e a confirmação.");
        }
        if (!senha.equals(senha2)) {
            return devolverComErro(model, f, "As senhas não conferem.");
        }

        try {
            service.cadastrar(f);
            redirectAttrs.addAttribute("msg", "cadastrado");
            return "redirect:/";
        } catch (RuntimeException e) {
            return devolverComErro(model, f, e.getMessage());
        }
    }

    private String devolverComErro(Model model, Farmaceutico f, String erro) {
        model.addAttribute("erro", erro);
        model.addAttribute("farmaceutico", f);
        model.addAttribute("ufs", UFS);
        return "cadastro";
    }
}
