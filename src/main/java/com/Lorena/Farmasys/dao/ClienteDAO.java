package com.Lorena.Farmasys.dao;

import com.Lorena.Farmasys.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente c) {
        String sql = "INSERT INTO cliente (nome, cpf, telefone, email, endereco, data_nascimento) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getEndereco());
            ps.setObject(6, c.getDataNascimento());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        System.out.println("Teste certo");
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
        return null;
    }

    public void atualizar(Cliente c) {
        String sql = "UPDATE cliente SET nome=?, cpf=?, telefone=?, email=?, endereco=?, " +
                     "data_nascimento=? WHERE id=?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getEndereco());
            ps.setObject(6, c.getDataNascimento());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    public void excluir(int id) {
        if (possuiVendas(id)) {
            throw new IllegalStateException(
                "Não é possível excluir: este cliente possui vendas registradas.");
        }
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }

    /** Indica se o cliente já está vinculado a alguma venda. */
    private boolean possuiVendas(int id) {
        String sql = "SELECT COUNT(*) FROM venda WHERE cliente_id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar vendas do cliente: " + e.getMessage(), e);
        }
    }

    private Cliente map(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("endereco"),
            rs.getObject("data_nascimento", LocalDate.class)
        );
    }
}
