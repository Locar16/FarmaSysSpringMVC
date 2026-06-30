package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Medicamento;
import com.Lorena.Farmasys.service.MedicamentoService;
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
@RequestMapping({"/medicamento", "/medicamentos"})
public class MedicamentoController {

    private static final List<String> CLASSIFICACOES = List.of(
        "ISENTO",
        "A1", "A2", "A3",
        "B1", "B2",
        "C1", "C2", "C3", "C4", "C5",
        "D1", "D2",
        "E",
        "F1", "F2", "F3", "F4"
    );

    private static final List<String> TIPOS = List.of("REFERENCIA", "GENERICO", "SIMILAR", "OUTRO");

    private final MedicamentoService service = new MedicamentoService();

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
            model.addAttribute("medicamento", service.buscarPorId(id));

        } else if ("excluir".equals(acao) && id != null) {
            try {
                service.excluir(id);
                redirectAttrs.addAttribute("msg", "Medicamento excluído com sucesso.");
            } catch (Exception e) {
                redirectAttrs.addAttribute("erro", e.getMessage());
            }
            return "redirect:/medicamento";
        }

        model.addAttribute("lista", service.listar());
        model.addAttribute("classificacoes", CLASSIFICACOES);
        model.addAttribute("tipos", TIPOS);
        return "medicamentos";
    }

    @PostMapping
    public String salvar(@RequestParam(required = false) Integer id,
                         @RequestParam String nome,
                         @RequestParam(name = "principio_ativo") String principioAtivo,
                         @RequestParam(required = false) String dosagem,
                         @RequestParam(required = false) String laboratorio,
                         @RequestParam String lote,
                         @RequestParam String classificacao,
                         @RequestParam(required = false) String tipo,
                         @RequestParam(name = "grupo_farmacologico", required = false) String grupoFarmacologico,
                         @RequestParam(required = false) String validade,
                         @RequestParam(name = "preco_venda", required = false) String precoVenda,
                         @RequestParam(name = "estoque_atual", required = false) String estoqueAtual,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttrs) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        boolean isUpdate = id != null;

        Medicamento m = new Medicamento();
        if (isUpdate) m.setId(id);

        m.setNome(nome);
        m.setPrincipioAtivo(principioAtivo);
        m.setDosagem(dosagem);
        m.setLaboratorio(laboratorio);
        m.setLote(lote);
        m.setClassificacao(classificacao);
        m.setTipo(tipo != null && !tipo.isBlank() ? tipo : null);
        m.setGrupoFarmacologico(grupoFarmacologico);

        if (validade != null && !validade.isBlank())
            m.setValidade(LocalDate.parse(validade));

        if (precoVenda != null && !precoVenda.isBlank())
            m.setPrecoVenda(new BigDecimal(precoVenda.replace(",", ".")));

        if (estoqueAtual != null && !estoqueAtual.isBlank())
            m.setEstoqueAtual(Integer.parseInt(estoqueAtual));

        try {
            String msg;
            if (isUpdate) {
                service.atualizar(m);
                msg = "Medicamento atualizado com sucesso.";
            } else {
                service.inserir(m);
                msg = "Medicamento cadastrado com sucesso.";
            }
            redirectAttrs.addAttribute("msg", msg);
            return "redirect:/medicamento";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("medicamento", m);
            model.addAttribute("lista", service.listar());
            model.addAttribute("classificacoes", CLASSIFICACOES);
            model.addAttribute("tipos", TIPOS);
            return "medicamentos";
        }
    }
}
