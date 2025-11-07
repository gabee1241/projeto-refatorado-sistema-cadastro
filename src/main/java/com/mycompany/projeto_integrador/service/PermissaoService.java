package com.mycompany.projeto_integrador.service;

import com.mycompany.projeto_integrador.model.Usuario;

public class PermissaoService {

    private void validarTipo(Usuario u) {
        if (u.getTipoUsuario() == null || u.getTipoUsuario().isBlank()) {
            throw new IllegalArgumentException("Tipo de usuário não pode ser nulo ou vazio");
        }

        String tipo = u.getTipoUsuario();
        if (!tipo.equalsIgnoreCase("Administrador") &&
            !tipo.equalsIgnoreCase("Operador") &&
            !tipo.equalsIgnoreCase("Usuario")) {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
    }

    public boolean podeCadastrar(Usuario u) {
        validarTipo(u);
        return u.getTipoUsuario().equalsIgnoreCase("Administrador");
    }

    public boolean podeConsultar(Usuario u) {
        validarTipo(u);
        return u.getTipoUsuario().equalsIgnoreCase("Administrador")
                || u.getTipoUsuario().equalsIgnoreCase("Operador");
    }
}
