package com.Lorena.Farmasys.service;

import com.Lorena.Farmasys.dao.ClienteDAO;
import com.Lorena.Farmasys.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteDAO dao = new ClienteDAO();

    public void inserir(Cliente c) {
        validar(c);
        try {
            dao.inserir(c);
        } catch (RuntimeException e) {
            traduzirDuplicidade(e);
            throw e;
        }
    }

    public List<Cliente> listar() {
        return dao.listar();
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void atualizar(Cliente c) {
        validar(c);
        try {
            dao.atualizar(c);
        } catch (RuntimeException e) {
            traduzirDuplicidade(e);
            throw e;
        }
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    private void validar(Cliente c) {
        if (c.getNome() == null || c.getNome().isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (c.getCpf() == null || c.getCpf().isBlank())
            throw new IllegalArgumentException("CPF é obrigatório.");
    }

    private void traduzirDuplicidade(Throwable e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
                throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
            }
            causa = causa.getCause();
        }
    }
}
