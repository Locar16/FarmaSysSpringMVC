package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Farmaceutico;
import service.FarmaceuticoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/farmaceutico")
public class FarmaceuticoServlet extends HttpServlet {

    private static final List<String> UFS = List.of(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    );

    private final FarmaceuticoService service = new FarmaceuticoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("farmaceutico") == null) {
            resp.sendRedirect("index.jsp");
            return;
        }

        String acao = req.getParameter("acao");

        Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");

        if ("editar".equals(acao)) {
            int idPedido = Integer.parseInt(req.getParameter("id"));

            if (logado.getId() != idPedido) {
                resp.sendRedirect(req.getContextPath() + "/farmaceutico");
                return;
            }

            req.setAttribute("farmaceuticoEdicao", service.buscarPorId(idPedido));
        }

        req.setAttribute("lista", service.listarAtivos());
        req.setAttribute("ufs", UFS);
        req.getRequestDispatcher("/WEB-INF/pages/farmaceutico.jsp").forward(req, resp);
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

        Farmaceutico logado = (Farmaceutico) session.getAttribute("farmaceutico");
        int idPedido = Integer.parseInt(req.getParameter("id"));

        if (logado.getId() != idPedido) {
            resp.sendRedirect(req.getContextPath() + "/farmaceutico");
            return;
        }

        if ("desativarConta".equals(req.getParameter("acao"))) {
            service.desativar(idPedido);
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        String senhaAtual = req.getParameter("senhaAtual");
        String novaSenha  = req.getParameter("novaSenha");
        String novaSenha2 = req.getParameter("novaSenha2");

        if (!logado.getSenha().equals(senhaAtual)) {
            req.setAttribute("erroSenha", "Senha atual incorreta.");
            reexibirEdicao(req, resp, logado.getId());
            return;
        }

        String senhaParaSalvar;
        if (novaSenha != null && !novaSenha.isBlank()) {
            if (!novaSenha.equals(novaSenha2)) {
                req.setAttribute("erroSenha", "As senhas não conferem.");
                reexibirEdicao(req, resp, logado.getId());
                return;
            }
            senhaParaSalvar = novaSenha;
        } else {
            senhaParaSalvar = logado.getSenha();
        }

        Farmaceutico f = new Farmaceutico();
        f.setId(idPedido);
        f.setUsuario(req.getParameter("usuario"));
        f.setNome(req.getParameter("nome"));
        f.setCpf(req.getParameter("cpf"));
        f.setCrfNumero(req.getParameter("crf_numero"));
        f.setCrfUf(req.getParameter("crf_uf"));
        f.setEmail(req.getParameter("email"));
        f.setTelefone(req.getParameter("telefone"));
        f.setSenha(senhaParaSalvar);
        f.setAtivo(logado.isAtivo());

        String salarioStr = req.getParameter("salario");
        if (salarioStr != null && !salarioStr.isBlank())
            f.setSalario(new BigDecimal(salarioStr.replace(",", ".")));

        String dataStr = req.getParameter("data_admissao");
        if (dataStr != null && !dataStr.isBlank())
            f.setDataAdmissao(LocalDate.parse(dataStr));

        try {
            service.atualizar(f);
            session.setAttribute("farmaceutico", service.buscarPorId(idPedido));
            resp.sendRedirect(req.getContextPath() + "/farmaceutico?msg=editado");
        } catch (Exception e) {
            req.setAttribute("erroSenha", e.getMessage());
            reexibirEdicao(req, resp, logado.getId());
        }
    }

    private void reexibirEdicao(HttpServletRequest req, HttpServletResponse resp, int id)
            throws ServletException, IOException {
        req.setAttribute("farmaceuticoEdicao", service.buscarPorId(id));
        req.setAttribute("lista", service.listarAtivos());
        req.setAttribute("ufs", UFS);
        req.getRequestDispatcher("/WEB-INF/pages/farmaceutico.jsp").forward(req, resp);
    }
}
