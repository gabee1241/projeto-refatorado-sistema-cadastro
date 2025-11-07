package com.mycompany.projeto_integrador.service;

import com.mycompany.projeto_integrador.exception.ServiceException;
import com.mycompany.projeto_integrador.model.Registro;

import java.util.List;
import java.util.Optional;

public interface RegistroService {
    Registro cadastrar(Registro r) throws ServiceException;
    Optional<Registro> obterPorId(int id) throws ServiceException;
    List<Registro> listar() throws ServiceException;
    boolean deletar(int id) throws ServiceException;
}
