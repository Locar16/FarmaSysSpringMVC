package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Medicamento;
import service.MedicamentoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@WebServlet({"/medicamento", "/medicamentos"})
public class MedicamentoServlet extends HttpServlet {

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("farmaceutico") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String acao = req.getParameter("acao");

        if ("editar".equals(acao)) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("medicamento", service.buscarPorId(id));

        } else if ("excluir".equals(acao)) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                service.excluir(id);
                resp.sendRedirect(req.getContextPath() + "/medicamento?msg=" +
                    URLEncoder.encode("Medicamento excluído com sucesso.", StandardCharsets.UTF_8));
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/medicamento?erro=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            }
            return;
        }

        req.setAttribute("lista", service.listar());
        req.setAttribute("classificacoes", CLASSIFICACOES);
        req.setAttribute("tipos", TIPOS);
        req.getRequestDispatcher("/WEB-INF/pages/medicamentos.jsp").forward(req, resp);
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

        String idParam = req.getParameter("id");
        boolean isUpdate = idParam != null && !idParam.isBlank();

        Medicamento m = new Medicamento();
        if (isUpdate) m.setId(Integer.parseInt(idParam));

        m.setNome(req.getParameter("nome"));
        m.setPrincipioAtivo(req.getParameter("principio_ativo"));
        m.setDosagem(req.getParameter("dosagem"));
        m.setLaboratorio(req.getParameter("laboratorio"));
        m.setLote(req.getParameter("lote"));
        m.setClassificacao(req.getParameter("classificacao"));

        String tipoStr = req.getParameter("tipo");
        m.setTipo(tipoStr != null && !tipoStr.isBlank() ? tipoStr : null);

        m.setGrupoFarmacologico(req.getParameter("grupo_farmacologico"));

        String validadeStr = req.getParameter("validade");
        if (validadeStr != null && !validadeStr.isBlank())
            m.setValidade(LocalDate.parse(validadeStr));

        String precoStr = req.getParameter("preco_venda");
        if (precoStr != null && !precoStr.isBlank())
            m.setPrecoVenda(new BigDecimal(precoStr.replace(",", ".")));

        String estoqueAtualStr = req.getParameter("estoque_atual");
        if (estoqueAtualStr != null && !estoqueAtualStr.isBlank())
            m.setEstoqueAtual(Integer.parseInt(estoqueAtualStr));

        try {
            String msg;
            if (isUpdate) {
                service.atualizar(m);
                msg = "Medicamento atualizado com sucesso.";
            } else {
                service.inserir(m);
                msg = "Medicamento cadastrado com sucesso.";
            }
            resp.sendRedirect(req.getContextPath() + "/medicamento?msg=" +
                URLEncoder.encode(msg, StandardCharsets.UTF_8));

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("medicamento", m);
            req.setAttribute("lista", service.listar());
            req.setAttribute("classificacoes", CLASSIFICACOES);
            req.setAttribute("tipos", TIPOS);
            req.getRequestDispatcher("/WEB-INF/pages/medicamentos.jsp").forward(req, resp);
        }
    }
}
