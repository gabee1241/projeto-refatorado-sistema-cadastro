package com.mycompany.projeto_integrador.service;

import com.mycompany.projeto_integrador.dao.UsuarioDAO;
import com.mycompany.projeto_integrador.dao.UsuarioDAOJdbc;
import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Usuario;

import java.util.List;
import java.util.Optional;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAOJdbc();

    @Override
    public Usuario cadastrar(Usuario u) throws ServiceException {
        try {
            // simples validação
            if (u.getNome() == null || u.getNome().isBlank()) throw new ServiceException("Nome obrigatório", null);
            return dao.salvar(u);
        } catch (Exception e) {
            throw new ServiceException("Erro ao cadastrar usuario", e);
        }
    }

    @Override
    public Optional<Usuario> obterPorId(int id) throws ServiceException {
        try { return dao.buscarPorId(id); } catch (Exception e) { throw new ServiceException("Erro ao buscar usuario", e); }
    }

    @Override
    public List<Usuario> listar() throws ServiceException {
        try { return dao.listar(); } catch (Exception e) { throw new ServiceException("Erro ao listar usuarios", e); }
    }

    @Override
    public boolean deletar(int id) throws ServiceException {
        try { return dao.deletar(id); } catch (Exception e) { throw new ServiceException("Erro ao deletar usuario", e); }
    }
}
