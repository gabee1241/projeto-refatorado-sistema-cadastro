package Main.service;

import Main.model.Usuario.Perfil;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {
    
    public boolean podeConsultar(Perfil perfil) {
        return perfil == Perfil.ADMIN || perfil == Perfil.OPERADOR || perfil == Perfil.USUARIO;
    }
    
    public boolean podeCadastrar(Perfil perfil) {
        return perfil == Perfil.ADMIN || perfil == Perfil.OPERADOR;
    }
    
    public boolean podeEditar(Perfil perfil) {
        return perfil == Perfil.ADMIN || perfil == Perfil.OPERADOR;
    }
    
    public boolean podeExcluir(Perfil perfil) {
        return perfil == Perfil.ADMIN;
    }
}