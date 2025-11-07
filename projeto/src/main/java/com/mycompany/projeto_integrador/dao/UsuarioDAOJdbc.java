package com.mycompany.projeto_integrador.dao;

import com.mycompany.projeto_integrador.exception.DaoException;
import com.mycompany.projeto_integrador.model.Usuario;
import com.mycompany.projeto_integrador.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOJdbc implements UsuarioDAO {

    @Override
    public Usuario salvar(Usuario u) throws DaoException {
        String sql = "INSERT INTO usuario (nome, senha, tipo_usuario, tipo) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getSenha());
            ps.setString(3, u.getTipoUsuario());
            ps.setString(4, u.getTipo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
            return u;
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar usuario", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) throws DaoException {
        String sql = "SELECT id, nome, senha, tipo_usuario, tipo FROM usuario WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setSenha(rs.getString("senha"));
                    u.setTipoUsuario(rs.getString("tipo_usuario"));
                    u.setTipo(rs.getString("tipo"));
                    return Optional.of(u);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar usuario", e);
        }
    }

    @Override
    public List<Usuario> listar() throws DaoException {
        String sql = "SELECT id, nome, senha, tipo_usuario, tipo FROM usuario";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Usuario> list = new ArrayList<>();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setSenha(rs.getString("senha"));
                u.setTipoUsuario(rs.getString("tipo_usuario"));
                u.setTipo(rs.getString("tipo"));
                list.add(u);
            }
            return list;
        } catch (Exception e) {
            throw new DaoException("Erro ao listar usuarios", e);
        }
    }

    @Override
    public boolean deletar(int id) throws DaoException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao deletar usuario", e);
        }
    }
    
    
    public Usuario buscarUsuario(String nome, String senha) {
    Usuario usuario = null;
    String sql = "SELECT id, nome, senha, tipo_usuario, tipo FROM usuario WHERE nome = ? AND senha = ?";
    
    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {
        
        ps.setString(1, nome);
        ps.setString(2, senha);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario")); // admin, operador, usuario
                usuario.setTipo(rs.getString("tipo"));
            }
        }
    } catch (Exception e) {    
    }
    return usuario;
}

}
