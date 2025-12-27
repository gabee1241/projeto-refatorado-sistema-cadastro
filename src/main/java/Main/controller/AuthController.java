package Main.controller;

import Main.model.Usuario;
import Main.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UsuarioService service;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String senha = credentials.get("senha");
        
        Optional<Usuario> usuario = service.autenticar(username, senha);
        
        if (usuario.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.get().getId());
            response.put("username", usuario.get().getUsername());
            response.put("perfil", usuario.get().getPerfil());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}