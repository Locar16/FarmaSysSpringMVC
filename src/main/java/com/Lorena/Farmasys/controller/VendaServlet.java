package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Farmaceutico;
import model.ItemVenda;
import model.Medicamento;
import model.Venda;
import service.ClienteService;
import service.MedicamentoService;
import service.VendaService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/venda")
public class VendaServlet extends HttpServlet {

    private final VendaService vendaService = new VendaService();
    private final MedicamentoService medicamentoService = new MedicamentoService();
    private final ClienteService clienteService = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("farmaceutico") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        carregarPaginaVenda(req, resp, session);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("farmaceutico") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String acao = req.getParameter("acao");

        if ("adicionar".equals(acao)) {
            adicionarItem(req, resp, session);
        } else if ("remover".equals(acao)) {
            removerItem(req, resp, session);
        } else if ("finalizar".equals(acao)) {
            finalizarVenda(req, resp, session);
        } else {
            resp.sendRedirect(req.getContextPath() + "/venda");
        }
    }

    private void adicionarItem(HttpServletRequest req, HttpServletResponse resp,
                                HttpSession session) throws IOException, ServletException {
        String medIdStr  = req.getParameter("medicamento_id");
        String qtdStr    = req.getParameter("quantidade");

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

            resp.sendRedirect(req.getContextPath() + "/venda");

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            carregarPaginaVenda(req, resp, session);
        }
    }

    private void removerItem(HttpServletRequest req, HttpServletResponse resp,
                              HttpSession session) throws IOException {
        try {
            int indice = Integer.parseInt(req.getParameter("indice"));
            List<ItemVenda> carrinho = obterCarrinho(session);
            if (indice >= 0 && indice < carrinho.size()) {
                carrinho.remove(indice);
            }
        } catch (NumberFormatException ignored) { /* indice inválido, ignora */ }
        resp.sendRedirect(req.getContextPath() + "/venda");
    }

    private void finalizarVenda(HttpServletRequest req, HttpServletResponse resp,
                                 HttpSession session) throws IOException, ServletException {
        List<ItemVenda> carrinho = obterCarrinho(session);
        String formaPagamento = req.getParameter("forma_pagamento");

        try {
            if (formaPagamento == null || formaPagamento.isBlank())
                throw new IllegalArgumentException("Selecione a forma de pagamento.");

            Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");

            Venda venda = new Venda();
            venda.setFormaPagamento(formaPagamento);
            venda.setItens(new ArrayList<>(carrinho));

            String clienteIdStr = req.getParameter("cliente_id");
            if (clienteIdStr != null && !clienteIdStr.isBlank()) {
                venda.setClienteId(Integer.parseInt(clienteIdStr));
            }

            vendaService.registrar(venda, logado);

            session.removeAttribute("carrinho");

            String msg = URLEncoder.encode("Venda registrada com sucesso! Total: R$ " +
                String.format("%.2f", venda.getValorTotal()), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/venda?msg=" + msg);

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            carregarPaginaVenda(req, resp, session);
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

    private void carregarPaginaVenda(HttpServletRequest req, HttpServletResponse resp,
                                      HttpSession session) throws ServletException, IOException {
        List<ItemVenda> carrinho = obterCarrinho(session);

        java.math.BigDecimal total = carrinho.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        req.setAttribute("carrinho", carrinho);
        req.setAttribute("totalCarrinho", total);
        req.setAttribute("medicamentos", medicamentoService.listar());
        req.setAttribute("clientes", clienteService.listar());
        req.getRequestDispatcher("/WEB-INF/pages/venda.jsp").forward(req, resp);
    }
}
