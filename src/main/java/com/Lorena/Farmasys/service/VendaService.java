package com.Lorena.Farmasys.service;

import com.Lorena.Farmasys.dao.MedicamentoDAO;
import com.Lorena.Farmasys.dao.VendaDAO;
import com.Lorena.Farmasys.model.Farmaceutico;
import com.Lorena.Farmasys.model.ItemVenda;
import com.Lorena.Farmasys.model.Medicamento;
import com.Lorena.Farmasys.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VendaService {

    private final VendaDAO dao = new VendaDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    public void registrar(Venda venda, Farmaceutico logado) {
        List<ItemVenda> itens = venda.getItens();
        if (itens == null || itens.isEmpty())
            throw new IllegalArgumentException("A venda deve ter pelo menos um item.");

        for (ItemVenda item : itens) {
            if (item.getQuantidade() <= 0)
                throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
            Medicamento med = medicamentoDAO.buscarPorId(item.getMedicamentoId());
            if (med == null)
                throw new IllegalArgumentException("Medicamento não encontrado.");
            if (med.getEstoqueAtual() < item.getQuantidade())
                throw new IllegalArgumentException(
                    "Estoque insuficiente para \"" + med.getNome() + "\". " +
                    "Disponível: " + med.getEstoqueAtual() + ".");
        }

        BigDecimal total = itens.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setValorTotal(total);
        venda.setDataHora(LocalDateTime.now());
        venda.setFarmaceuticoId(logado.getId());

        dao.salvar(venda);
    }

    public List<Venda> listar() {
        return dao.listar();
    }

    public List<Venda> listarComDetalhes() {
        return dao.listarComDetalhes();
    }

    public Venda buscarPorId(int id) {
        return dao.buscarPorId(id);
    }
}
