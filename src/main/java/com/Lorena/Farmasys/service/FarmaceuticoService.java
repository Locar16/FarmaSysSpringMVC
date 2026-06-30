package com.Lorena.Farmasys.service;

import com.Lorena.Farmasys.dao.FarmaceuticoDAO;
import com.Lorena.Farmasys.model.Farmaceutico;

import java.sql.SQLException;
import java.util.List;

public class FarmaceuticoService {

    private final FarmaceuticoDAO dao = new FarmaceuticoDAO();

    public void inserir(Farmaceutico f) {
        validar(f);
        dao.inserir(f);
    }

    public void cadastrar(Farmaceutico f) {
        validar(f);
        if (f.getSenha() == null || f.getSenha().isBlank())
            throw new IllegalArgumentException("Senha é obrigatória.");
        if (f.getCrfUf() == null || f.getCrfUf().isBlank())
            throw new IllegalArgumentException("UF do CRF é obrigatória.");

        f.setAtivo(true);

        try {
            dao.inserir(f);
        } catch (RuntimeException e) {
            traduzirDuplicidade(e);
            throw e;
        }
    }

    private void traduzirDuplicidade(Throwable e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
                String detalhe = sql.getMessage() == null ? "" : sql.getMessage().toLowerCase();
                if (detalhe.contains("usuario"))
                    throw new IllegalArgumentException("Este usuário já está em uso. Escolha outro.");
                if (detalhe.contains("cpf"))
                    throw new IllegalArgumentException("Já existe um cadastro com este CPF.");
                if (detalhe.contains("crf"))
                    throw new IllegalArgumentException("Já existe um cadastro com este CRF (número e UF).");
                throw new IllegalArgumentException("Já existe um cadastro com estes dados.");
            }
            causa = causa.getCause();
        }
    }

    public List<Farmaceutico> listar() {
        return dao.listar();
    }

    public List<Farmaceutico> listarAtivos() {
        return dao.listarAtivos();
    }

    public Farmaceutico buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void atualizar(Farmaceutico f) {
        validar(f);
        dao.atualizar(f);
    }

    public void desativar(int id) {
        dao.desativar(id);
    }

    public Farmaceutico autenticar(String usuario, String senha) {
        return dao.autenticar(usuario, senha);
    }

    private void validar(Farmaceutico f) {
        if (f.getUsuario() == null || f.getUsuario().isBlank())
            throw new IllegalArgumentException("Usuário é obrigatório.");
        if (f.getCpf() == null || f.getCpf().isBlank())
            throw new IllegalArgumentException("CPF é obrigatório.");
        if (f.getCrfNumero() == null || f.getCrfNumero().isBlank())
            throw new IllegalArgumentException("Número do CRF é obrigatório.");
        if (f.getNome() == null || f.getNome().isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
    }
}
