package com.mycompany.projeto_integrador.dao;

import com.mycompany.projeto_integrador.exception.DaoException;
import com.mycompany.projeto_integrador.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {
    Usuario salvar(Usuario u) throws DaoException;
    Optional<Usuario> buscarPorId(int id) throws DaoException;
    List<Usuario> listar() throws DaoException;
    boolean deletar(int id) throws DaoException;
}
