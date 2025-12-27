package Main.service;

import Main.model.Usuario;
import Main.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository repository;
    
    public Optional<Usuario> autenticar(String username, String senha) {
        Optional<Usuario> usuario = repository.findByUsername(username);
        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            return usuario;
        }
        return Optional.empty();
    }
    
    public Usuario criar(Usuario usuario) {
        return repository.save(usuario);
    }
}