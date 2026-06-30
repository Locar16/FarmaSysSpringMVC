package com.Lorena.Farmasys.dao;

import model.ItemVenda;
import model.Venda;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void salvar(Venda venda) {
        String sqlVenda   = "INSERT INTO venda (data_hora, valor_total, forma_pagamento, farmaceutico_id, cliente_id) " +
                            "VALUES (?, ?, ?::forma_pagamento, ?, ?)";
        String sqlItem    = "INSERT INTO item_venda (venda_id, medicamento_id, quantidade, valor_unitario) " +
                            "VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE medicamento SET estoque_atual = estoque_atual - ? WHERE id = ?";

        try (Connection con = ConexaoDB.getConexao()) {
            int vendaId;
            try (PreparedStatement ps = con.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, venda.getDataHora());
                ps.setBigDecimal(2, venda.getValorTotal());
                ps.setString(3, venda.getFormaPagamento());
                ps.setInt(4, venda.getFarmaceuticoId());
                ps.setObject(5, venda.getClienteId(), Types.INTEGER);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    vendaId = rs.getInt(1);
                }
            }
            venda.setId(vendaId);

            for (ItemVenda item : venda.getItens()) {
                try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                    ps.setInt(1, vendaId);
                    ps.setInt(2, item.getMedicamentoId());
                    ps.setInt(3, item.getQuantidade());
                    ps.setBigDecimal(4, item.getValorUnitario());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sqlEstoque)) {
                    ps.setInt(1, item.getQuantidade());
                    ps.setInt(2, item.getMedicamentoId());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar venda: " + e.getMessage(), e);
        }
    }

    public List<Venda> listar() {
        String sql = "SELECT * FROM venda ORDER BY data_hora DESC";
        List<Venda> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }
        return lista;
    }

    public Venda buscarPorId(int id) {
        String sqlVenda = "SELECT * FROM venda WHERE id = ?";
        String sqlItens = "SELECT iv.*, m.nome AS med_nome " +
                          "FROM item_venda iv " +
                          "JOIN medicamento m ON m.id = iv.medicamento_id " +
                          "WHERE iv.venda_id = ?";
        try (Connection con = ConexaoDB.getConexao()) {
            Venda venda = null;
            try (PreparedStatement ps = con.prepareStatement(sqlVenda)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) venda = map(rs);
                }
            }
            if (venda == null) return null;

            try (PreparedStatement ps = con.prepareStatement(sqlItens)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) venda.getItens().add(mapItem(rs));
                }
            }
            return venda;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar venda: " + e.getMessage(), e);
        }
    }

    public List<Venda> listarComDetalhes() {
        String sqlVendas =
            "SELECT v.*, f.nome AS farmaceutico_nome, cl.nome AS cliente_nome " +
            "FROM venda v " +
            "JOIN farmaceutico f ON f.id = v.farmaceutico_id " +
            "LEFT JOIN cliente cl ON cl.id = v.cliente_id " +
            "ORDER BY v.data_hora DESC";
        String sqlItens =
            "SELECT iv.*, m.nome AS med_nome " +
            "FROM item_venda iv " +
            "JOIN medicamento m ON m.id = iv.medicamento_id " +
            "WHERE iv.venda_id = ? ORDER BY iv.id";

        List<Venda> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao()) {
            try (PreparedStatement ps = con.prepareStatement(sqlVendas);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapComDetalhes(rs));
            }
            try (PreparedStatement psItens = con.prepareStatement(sqlItens)) {
                for (Venda v : lista) {
                    psItens.setInt(1, v.getId());
                    try (ResultSet rsItens = psItens.executeQuery()) {
                        while (rsItens.next()) v.getItens().add(mapItem(rsItens));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas com detalhes: " + e.getMessage(), e);
        }
        return lista;
    }

    private Venda map(ResultSet rs) throws SQLException {
        Venda v = new Venda(
            rs.getInt("id"),
            rs.getObject("data_hora", LocalDateTime.class),
            rs.getBigDecimal("valor_total"),
            rs.getString("forma_pagamento"),
            rs.getInt("farmaceutico_id")
        );
        int cid = rs.getInt("cliente_id");
        if (!rs.wasNull()) v.setClienteId(cid);
        return v;
    }

    private Venda mapComDetalhes(ResultSet rs) throws SQLException {
        Venda v = map(rs);
        v.setFarmaceuticoNome(rs.getString("farmaceutico_nome"));
        v.setClienteNome(rs.getString("cliente_nome"));
        return v;
    }

    private ItemVenda mapItem(ResultSet rs) throws SQLException {
        return new ItemVenda(
            rs.getInt("id"),
            rs.getInt("venda_id"),
            rs.getInt("medicamento_id"),
            rs.getString("med_nome"),
            rs.getInt("quantidade"),
            rs.getBigDecimal("valor_unitario")
        );
    }
}
