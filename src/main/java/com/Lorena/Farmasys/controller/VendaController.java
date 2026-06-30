package com.Lorena.Farmasys.controller;

import com.Lorena.Farmasys.model.Farmaceutico;
import com.Lorena.Farmasys.model.ItemVenda;
import com.Lorena.Farmasys.model.Medicamento;
import com.Lorena.Farmasys.model.Venda;
import com.Lorena.Farmasys.service.ClienteService;
import com.Lorena.Farmasys.service.MedicamentoService;
import com.Lorena.Farmasys.service.VendaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/venda")
public class VendaController {

    private final VendaService vendaService = new VendaService();
    private final MedicamentoService medicamentoService = new MedicamentoService();
    private final ClienteService clienteService = new ClienteService();

    @GetMapping
    public String exibir(HttpSession session, Model model) {
        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }
        return carregarPaginaVenda(session, model);
    }

    @PostMapping
    public String processar(@RequestParam(required = false) String acao,
                            @RequestParam(name = "medicamento_id", required = false) String medicamentoId,
                            @RequestParam(required = false) String quantidade,
                            @RequestParam(required = false) String indice,
                            @RequestParam(name = "forma_pagamento", required = false) String formaPagamento,
                            @RequestParam(name = "cliente_id", required = false) String clienteId,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttrs) {

        if (session.getAttribute("farmaceutico") == null) {
            return "redirect:/";
        }

        if ("adicionar".equals(acao)) {
            return adicionarItem(medicamentoId, quantidade, session, model);
        } else if ("remover".equals(acao)) {
            return removerItem(indice, session);
        } else if ("finalizar".equals(acao)) {
            return finalizarVenda(formaPagamento, clienteId, session, model, redirectAttrs);
        } else {
            return "redirect:/venda";
        }
    }

    private String adicionarItem(String medIdStr, String qtdStr, HttpSession session, Model model) {
        try {
            int medId = Integer.parseInt(medIdStr);
            int qtd   = Integer.parseInt(qtdStr);
            if (qtd <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero.");

            Medicamento med = medicamentoService.buscarPorId(medId);
            if (med == null) throw new IllegalArgumentException("Medicamento não encontrado.");

            List<ItemVenda> carrinho = obterCarrinho(session);

            boolean encontrado = false;
            for (ItemVenda item : carrinho) {
                if (item.getMedicamentoId() == medId) {
                    item.setQuantidade(item.getQuantidade() + qtd);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                ItemVenda item = new ItemVenda();
                item.setMedicamentoId(medId);
                item.setMedicamentoNome(med.getNome());
                item.setMedicamentoClassificacao(med.getClassificacao());
                item.setQuantidade(qtd);
                item.setValorUnitario(med.getPrecoVenda());
                carrinho.add(item);
            }

            return "redirect:/venda";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return carregarPaginaVenda(session, model);
        }
    }

    private String removerItem(String indiceStr, HttpSession session) {
        try {
            int indice = Integer.parseInt(indiceStr);
            List<ItemVenda> carrinho = obterCarrinho(session);
            if (indice >= 0 && indice < carrinho.size()) {
                carrinho.remove(indice);
            }
        } catch (NumberFormatException ignored) { /* indice inválido, ignora */ }
        return "redirect:/venda";
    }

    private String finalizarVenda(String formaPagamento, String clienteIdStr,
                                  HttpSession session, Model model,
                                  RedirectAttributes redirectAttrs) {
        List<ItemVenda> carrinho = obterCarrinho(session);

        try {
            if (formaPagamento == null || formaPagamento.isBlank())
                throw new IllegalArgumentException("Selecione a forma de pagamento.");

            Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");

            Venda venda = new Venda();
            venda.setFormaPagamento(formaPagamento);
            venda.setItens(new ArrayList<>(carrinho));

            if (clienteIdStr != null && !clienteIdStr.isBlank()) {
                venda.setClienteId(Integer.parseInt(clienteIdStr));
            }

            vendaService.registrar(venda, logado);

            session.removeAttribute("carrinho");

            redirectAttrs.addAttribute("msg", "Venda registrada com sucesso! Total: R$ " +
                String.format("%.2f", venda.getValorTotal()));
            return "redirect:/venda";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return carregarPaginaVenda(session, model);
        }
    }

    @SuppressWarnings("unchecked")
    private List<ItemVenda> obterCarrinho(HttpSession session) {
        List<ItemVenda> carrinho = (List<ItemVenda>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }
        return carrinho;
    }

    private String carregarPaginaVenda(HttpSession session, Model model) {
        List<ItemVenda> carrinho = obterCarrinho(session);

        BigDecimal total = carrinho.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("totalCarrinho", total);
        model.addAttribute("medicamentos", medicamentoService.listar());
        model.addAttribute("clientes", clienteService.listar());
        return "venda";
    }
}
