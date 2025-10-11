package com.mycompany.projeto_integrador.service;

import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario cadastrar(Usuario u) throws ServiceException;
    Optional<Usuario> obterPorId(int id) throws ServiceException;
    List<Usuario> listar() throws ServiceException;
    boolean deletar(int id) throws ServiceException;
}
