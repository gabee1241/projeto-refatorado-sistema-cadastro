package com.mycompany.projeto_integrador.controller;

import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Usuario;
import com.mycompany.projeto_integrador.service.UsuarioService;
import com.mycompany.projeto_integrador.service.UsuarioServiceImpl;

import java.util.List;
import java.util.Optional;

public class UsuarioController {
    private final UsuarioService service = new UsuarioServiceImpl();

    public Usuario cadastrar(Usuario u) throws ServiceException {
        return service.cadastrar(u);
    }

    public Optional<Usuario> buscarPorId(int id) throws ServiceException {
        return service.obterPorId(id);
    }

    public List<Usuario> listar() throws ServiceException {
        return service.listar();
    }

    public boolean deletar(int id) throws ServiceException {
        return service.deletar(id);
    }
}
