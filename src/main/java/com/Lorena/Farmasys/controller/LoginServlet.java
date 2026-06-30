package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Farmaceutico;
import service.FarmaceuticoService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final FarmaceuticoService service = new FarmaceuticoService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String usuario = req.getParameter("usuario");
        String senha   = req.getParameter("senha");

        Farmaceutico f = service.autenticar(usuario, senha);
        if (f != null) {
            req.getSession().setAttribute("farmaceutico", f);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/?msg=" +
                URLEncoder.encode("Usuário ou senha inválidos.", StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
