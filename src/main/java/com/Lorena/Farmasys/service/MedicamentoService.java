package com.Lorena.Farmasys.service;

import dao.MedicamentoDAO;
import model.Medicamento;

import java.math.BigDecimal;
import java.util.List;

public class MedicamentoService {

    private final MedicamentoDAO dao = new MedicamentoDAO();

    public void inserir(Medicamento m) {
        validar(m);
        dao.inserir(m);
    }

    public List<Medicamento> listar() {
        return dao.listar();
    }

    public Medicamento buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void atualizar(Medicamento m) {
        validar(m);
        dao.atualizar(m);
    }

    public void excluir(int id) {
        dao.excluir(id);
    }

    private void validar(Medicamento m) {
        if (m.getNome() == null || m.getNome().isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (m.getLote() == null || m.getLote().isBlank())
            throw new IllegalArgumentException("Lote é obrigatório.");
        if (m.getValidade() == null)
            throw new IllegalArgumentException("Validade é obrigatória.");
        if (m.getPrecoVenda() == null || m.getPrecoVenda().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Preço de venda não pode ser negativo.");
        if (m.getEstoqueAtual() < 0)
            throw new IllegalArgumentException("Estoque atual não pode ser negativo.");
    }
}
