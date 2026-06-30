package com.Lorena.Farmasys.dao;

import model.Farmaceutico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FarmaceuticoDAO {

    public void inserir(Farmaceutico f) {
        String sql = "INSERT INTO farmaceutico " +
                     "(usuario, crf_numero, crf_uf, senha, nome, cpf, email, telefone, salario, data_admissao, ativo) " +
                     "VALUES (?, ?, ?::uf, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getUsuario());
            ps.setString(2, f.getCrfNumero());
            ps.setString(3, f.getCrfUf());
            ps.setString(4, f.getSenha());
            ps.setString(5, f.getNome());
            ps.setString(6, f.getCpf());
            ps.setString(7, f.getEmail());
            ps.setString(8, f.getTelefone());
            ps.setObject(9, f.getSalario());
            ps.setObject(10, f.getDataAdmissao());
            ps.setBoolean(11, f.isAtivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir farmacêutico: " + e.getMessage(), e);
        }
    }

    public List<Farmaceutico> listar() {
        String sql = "SELECT * FROM farmaceutico ORDER BY nome";
        List<Farmaceutico> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar farmacêuticos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Farmaceutico> listarAtivos() {
        String sql = "SELECT * FROM farmaceutico WHERE ativo = true ORDER BY nome";
        List<Farmaceutico> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar farmacêuticos ativos: " + e.getMessage(), e);
        }
        return lista;
    }

    public void desativar(int id) {
        String sql = "UPDATE farmaceutico SET ativo = false WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar farmacêutico: " + e.getMessage(), e);
        }
    }

    public Farmaceutico buscarPorId(int id) {
        String sql = "SELECT * FROM farmaceutico WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar farmacêutico: " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizar(Farmaceutico f) {
        boolean atualizarSenha = f.getSenha() != null && !f.getSenha().isBlank();

        String sql = atualizarSenha
            ? "UPDATE farmaceutico SET usuario=?, crf_numero=?, crf_uf=?::uf, senha=?, nome=?, cpf=?, " +
              "email=?, telefone=?, salario=?, data_admissao=? WHERE id=?"
            : "UPDATE farmaceutico SET usuario=?, crf_numero=?, crf_uf=?::uf, nome=?, cpf=?, " +
              "email=?, telefone=?, salario=?, data_admissao=? WHERE id=?";

        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getUsuario());
            ps.setString(2, f.getCrfNumero());
            ps.setString(3, f.getCrfUf());

            int i = 4;
            if (atualizarSenha) ps.setString(i++, f.getSenha());

            ps.setString(i++, f.getNome());
            ps.setString(i++, f.getCpf());
            ps.setString(i++, f.getEmail());
            ps.setString(i++, f.getTelefone());
            ps.setObject(i++, f.getSalario());
            ps.setObject(i++, f.getDataAdmissao());
            ps.setInt(i, f.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar farmacêutico: " + e.getMessage(), e);
        }
    }

    public Farmaceutico autenticar(String usuario, String senha) {
        String sql = "SELECT * FROM farmaceutico WHERE usuario = ? AND senha = ? AND ativo = true";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar farmacêutico: " + e.getMessage(), e);
        }
        return null;
    }

    private Farmaceutico map(ResultSet rs) throws SQLException {
        return new Farmaceutico(
            rs.getInt("id"),
            rs.getString("usuario"),
            rs.getString("crf_numero"),
            rs.getString("crf_uf"),
            rs.getString("senha"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getBigDecimal("salario"),
            rs.getObject("data_admissao", LocalDate.class),
            rs.getBoolean("ativo")
        );
    }
}
