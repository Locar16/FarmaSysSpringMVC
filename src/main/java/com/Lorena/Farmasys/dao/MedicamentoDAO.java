package com.Lorena.Farmasys.dao;

import model.Medicamento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

    public void inserir(Medicamento m) {
        String sql = "INSERT INTO medicamento " +
                     "(nome, principio_ativo, dosagem, laboratorio, lote, validade, classificacao, " +
                     "preco_venda, estoque_atual, tipo, grupo_farmacologico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?::classificacao_medicamento, ?, ?, ?::tipo_medicamento, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getPrincipioAtivo());
            ps.setString(3, m.getDosagem());
            ps.setString(4, m.getLaboratorio());
            ps.setString(5, m.getLote());
            ps.setObject(6, m.getValidade());
            ps.setString(7, m.getClassificacao());
            ps.setBigDecimal(8, m.getPrecoVenda());
            ps.setInt(9, m.getEstoqueAtual());
            ps.setObject(10, m.getTipo(), Types.OTHER);
            ps.setString(11, m.getGrupoFarmacologico());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir medicamento: " + e.getMessage(), e);
        }
    }

    public List<Medicamento> listar() {
        String sql = "SELECT * FROM medicamento ORDER BY nome";
        List<Medicamento> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar medicamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Medicamento buscarPorId(int id) {
        String sql = "SELECT * FROM medicamento WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar medicamento: " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizar(Medicamento m) {
        String sql = "UPDATE medicamento SET nome=?, principio_ativo=?, dosagem=?, laboratorio=?, " +
                     "lote=?, validade=?, classificacao=?::classificacao_medicamento, preco_venda=?, " +
                     "estoque_atual=?, tipo=?::tipo_medicamento, grupo_farmacologico=? WHERE id=?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNome());
            ps.setString(2, m.getPrincipioAtivo());
            ps.setString(3, m.getDosagem());
            ps.setString(4, m.getLaboratorio());
            ps.setString(5, m.getLote());
            ps.setObject(6, m.getValidade());
            ps.setString(7, m.getClassificacao());
            ps.setBigDecimal(8, m.getPrecoVenda());
            ps.setInt(9, m.getEstoqueAtual());
            ps.setObject(10, m.getTipo(), Types.OTHER);
            ps.setString(11, m.getGrupoFarmacologico());
            ps.setInt(12, m.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar medicamento: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        if (possuiVendas(id)) {
            throw new IllegalStateException(
                "Não é possível excluir: este medicamento possui vendas registradas.");
        }
        String sql = "DELETE FROM medicamento WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir medicamento: " + e.getMessage(), e);
        }
    }

    /** Indica se o medicamento já foi vendido (referenciado em item_venda). */
    private boolean possuiVendas(int id) {
        String sql = "SELECT COUNT(*) FROM item_venda WHERE medicamento_id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar vendas do medicamento: " + e.getMessage(), e);
        }
    }

    private Medicamento map(ResultSet rs) throws SQLException {
        return new Medicamento(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("principio_ativo"),
            rs.getString("dosagem"),
            rs.getString("laboratorio"),
            rs.getString("lote"),
            rs.getObject("validade", LocalDate.class),
            rs.getString("classificacao"),
            rs.getBigDecimal("preco_venda"),
            rs.getInt("estoque_atual"),
            rs.getString("tipo"),
            rs.getString("grupo_farmacologico")
        );
    }
}
