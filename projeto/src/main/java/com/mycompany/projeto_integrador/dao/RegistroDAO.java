package com.mycompany.projeto_integrador.dao;

import com.mycompany.projeto_integrador.exception.DaoException;
import com.mycompany.projeto_integrador.model.Registro;

import java.util.List;
import java.util.Optional;

public interface RegistroDAO {
    Registro salvar(Registro r) throws DaoException;
    Optional<Registro> buscarPorId(int id) throws DaoException;
    List<Registro> listar() throws DaoException;
    boolean deletar(int id) throws DaoException;
}
