package com.mycompany.projeto_integrador.service;

import com.mycompany.projeto_integrador.dao.RegistroDAO;
import com.mycompany.projeto_integrador.dao.RegistroDAOJdbc;
import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Registro;

import java.util.List;
import java.util.Optional;

public class RegistroServiceImpl implements RegistroService {

    private final RegistroDAO dao = new RegistroDAOJdbc();

    @Override
    public Registro cadastrar(Registro r) throws ServiceException {
        try {
            if (r.getNome() == null || r.getNome().isBlank()) throw new ServiceException("Nome obrigatório", null);
            return dao.salvar(r);
        } catch (Exception e) {
            throw new ServiceException("Erro ao cadastrar registro", e);
        }
    }

    @Override
    public Optional<Registro> obterPorId(int id) throws ServiceException {
        try { return dao.buscarPorId(id); } catch (Exception e) { throw new ServiceException("Erro ao buscar registro", e); }
    }

    @Override
    public List<Registro> listar() throws ServiceException {
        try { return dao.listar(); } catch (Exception e) { throw new ServiceException("Erro ao listar registros", e); }
    }

    @Override
    public boolean deletar(int id) throws ServiceException {
        try { return dao.deletar(id); } catch (Exception e) { throw new ServiceException("Erro ao deletar registro", e); }
    }
}
