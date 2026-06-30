package com.Lorena.Farmasys.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Farmaceutico;
import service.FarmaceuticoService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * Auto-cadastro PÚBLICO de farmacêutico. Servlet sem checagem de sessão:
 * qualquer visitante (não logado) pode criar a própria conta.
 */
@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private static final List<String> UFS = List.of(
        "AC","AL","AP","AM","BA","CE","DF","ES","GO","MA",
        "MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN",
        "RS","RO","RR","SC","SP","SE","TO"
    );

    private final FarmaceuticoService service = new FarmaceuticoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("ufs", UFS);
        req.getRequestDispatcher("/WEB-INF/pages/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Farmaceutico f = new Farmaceutico();
        f.setUsuario(req.getParameter("usuario"));
        f.setNome(req.getParameter("nome"));
        f.setCpf(req.getParameter("cpf"));
        f.setCrfNumero(req.getParameter("crf_numero"));
        f.setCrfUf(req.getParameter("crf_uf"));
        f.setEmail(req.getParameter("email"));
        f.setTelefone(req.getParameter("telefone"));

        String senha  = req.getParameter("senha");
        String senha2 = req.getParameter("senha2");
        f.setSenha(senha);

        String salarioStr = req.getParameter("salario");
        if (salarioStr != null && !salarioStr.isBlank())
            f.setSalario(new BigDecimal(salarioStr.replace(",", ".")));

        String dataStr = req.getParameter("data_admissao");
        if (dataStr != null && !dataStr.isBlank())
            f.setDataAdmissao(LocalDate.parse(dataStr));

        // Confirmação de senha validada no servidor antes de qualquer cadastro.
        if (senha == null || senha.isBlank() || senha2 == null || senha2.isBlank()) {
            devolverComErro(req, resp, f, "Informe a senha e a confirmação.");
            return;
        }
        if (!senha.equals(senha2)) {
            devolverComErro(req, resp, f, "As senhas não conferem.");
            return;
        }

        try {
            service.cadastrar(f);
            resp.sendRedirect(req.getContextPath() + "/index.jsp?msg=" +
                URLEncoder.encode("cadastrado", StandardCharsets.UTF_8));
        } catch (RuntimeException e) {
            devolverComErro(req, resp, f, e.getMessage());
        }
    }

    private void devolverComErro(HttpServletRequest req, HttpServletResponse resp,
                                 Farmaceutico f, String erro)
            throws ServletException, IOException {
        req.setAttribute("erro", erro);
        req.setAttribute("farmaceutico", f);
        req.setAttribute("ufs", UFS);
        req.getRequestDispatcher("/WEB-INF/pages/cadastro.jsp").forward(req, resp);
    }
}
