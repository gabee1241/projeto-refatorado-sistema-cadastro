package com.mycompany.projeto_integrador.dao;

import com.mycompany.projeto_integrador.exception.DaoException;
import com.mycompany.projeto_integrador.model.Registro;
import com.mycompany.projeto_integrador.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroDAOJdbc implements RegistroDAO {

    @Override
    public Registro salvar(Registro r) throws DaoException {
        String sql = "INSERT INTO registros (nome, identificacao, idade, curso_departamento) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getNome());
            ps.setString(2, r.getIdentificacao());
            ps.setInt(3, r.getIdade());
            ps.setString(4, r.getCursoDepartamento());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
            return r;
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar registro", e);
        }
    }

    @Override
    public Optional<Registro> buscarPorId(int id) throws DaoException {
        String sql = "SELECT id, nome, identificacao, idade, curso_departamento FROM registros WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Registro r = new Registro();
                    r.setId(rs.getInt("id"));
                    r.setNome(rs.getString("nome"));
                    r.setIdentificacao(rs.getString("identificacao"));
                    r.setIdade(rs.getInt("idade"));
                    r.setCursoDepartamento(rs.getString("curso_departamento"));
                    return Optional.of(r);
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar registro", e);
        }
    }

    @Override
    public List<Registro> listar() throws DaoException {
        String sql = "SELECT id, nome, identificacao, idade, curso_departamento FROM registros";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Registro> list = new ArrayList<>();
            while (rs.next()) {
                Registro r = new Registro();
                r.setId(rs.getInt("id"));
                r.setNome(rs.getString("nome"));
                r.setIdentificacao(rs.getString("identificacao"));
                r.setIdade(rs.getInt("idade"));
                r.setCursoDepartamento(rs.getString("curso_departamento"));
                list.add(r);
            }
            return list;
        } catch (Exception e) {
            throw new DaoException("Erro ao listar registros", e);
        }
    }

    @Override
    public boolean deletar(int id) throws DaoException {
        String sql = "DELETE FROM registros WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao deletar registro", e);
        }
    }
   // Sobrecarga para deletar passando o objeto Registro
    public boolean deletar(Registro registro) throws DaoException {
    if (registro == null || registro.getId() == 0) {
        throw new DaoException("Registro inválido para exclusão", null);
    }
    return deletar(registro.getId());
    }
    
    public List<Registro> filtrarPorRegistro(String curso_departamento) throws DaoException {
    List<Registro> lista = new ArrayList<>();
    String sql = "SELECT * FROM registros WHERE curso_departamento LIKE ?";

    try (Connection c = DBConnection.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, "%" + curso_departamento + "%");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Registro r = new Registro();
                r.setId(rs.getInt("id"));
                r.setNome(rs.getString("nome")); // ajuste para os campos reais
                r.setCursoDepartamento(rs.getString("curso_departamento")); 
                lista.add(r);
            }
        }

    } catch (Exception e) {
        throw new DaoException("Erro ao filtrar registros por curso/departamento", e);
    }

    return lista;
}

}
