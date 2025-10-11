package com.mycompany.projeto_integrador.controller;

import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Registro;
import com.mycompany.projeto_integrador.service.RegistroService;
import com.mycompany.projeto_integrador.service.RegistroServiceImpl;

import java.util.List;
import java.util.Optional;

public class RegistroController {
    private final RegistroService service = new RegistroServiceImpl();

    public Registro cadastrar(Registro r) throws ServiceException {
        return service.cadastrar(r);
    }

    public Optional<Registro> buscarPorId(int id) throws ServiceException {
        return service.obterPorId(id);
    }

    public List<Registro> listar() throws ServiceException {
        return service.listar();
    }

    public boolean deletar(int id) throws ServiceException {
        return service.deletar(id);
    }
}
