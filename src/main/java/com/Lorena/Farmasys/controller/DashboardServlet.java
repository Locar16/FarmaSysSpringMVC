package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.FarmaceuticoService;
import service.VendaService;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final FarmaceuticoService service = new FarmaceuticoService();
    private final VendaService vendaService = new VendaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("farmaceutico") == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        req.setAttribute("lista", service.listar());
        req.setAttribute("vendasRealizadas", vendaService.listarComDetalhes());
        req.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(req, resp);
    }
}
