package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cliente;
import service.ClienteService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {

    private final ClienteService service = new ClienteService();

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
            req.setAttribute("cliente", service.buscarPorId(id));

        } else if ("excluir".equals(acao)) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                service.excluir(id);
                resp.sendRedirect(req.getContextPath() + "/cliente?msg=" +
                    URLEncoder.encode("Cliente excluído com sucesso.", StandardCharsets.UTF_8));
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/cliente?erro=" +
                    URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            }
            return;
        }

        req.setAttribute("lista", service.listar());
        req.getRequestDispatcher("/WEB-INF/pages/clientes.jsp").forward(req, resp);
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

        Cliente c = new Cliente();
        if (isUpdate) c.setId(Integer.parseInt(idParam));

        c.setNome(req.getParameter("nome"));
        c.setCpf(req.getParameter("cpf"));
        c.setTelefone(req.getParameter("telefone"));
        c.setEmail(req.getParameter("email"));
        c.setEndereco(req.getParameter("endereco"));

        String dataNascStr = req.getParameter("data_nascimento");
        if (dataNascStr != null && !dataNascStr.isBlank())
            c.setDataNascimento(LocalDate.parse(dataNascStr));

        try {
            String msg;
            if (isUpdate) {
                service.atualizar(c);
                msg = "Cliente atualizado com sucesso.";
            } else {
                service.inserir(c);
                msg = "Cliente cadastrado com sucesso.";
            }
            resp.sendRedirect(req.getContextPath() + "/cliente?msg=" +
                URLEncoder.encode(msg, StandardCharsets.UTF_8));

        } catch (Exception e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("cliente", c);
            req.setAttribute("lista", service.listar());
            req.getRequestDispatcher("/WEB-INF/pages/clientes.jsp").forward(req, resp);
        }
    }
}
