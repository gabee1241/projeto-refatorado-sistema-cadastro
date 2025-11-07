package test;

import com.mycompany.projeto_integrador.model.Usuario;
import com.mycompany.projeto_integrador.service.PermissaoService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PermissaoUsuarioTest {

    private PermissaoService permissao;

    @BeforeEach
    public void setup() {
        permissao = new PermissaoService();
    }

    @Test
    public void testPermissaoAdministrador() {
        Usuario admin = new Usuario("admin","123","Administrador");
        assertTrue(permissao.podeCadastrar(admin));
        assertTrue(permissao.podeConsultar(admin));
    }

    @Test
    public void testPermissaoOperador() {
        Usuario operador = new Usuario("operador","123","Operador");
        assertFalse(permissao.podeCadastrar(operador));
        assertTrue(permissao.podeConsultar(operador));
    }

    @Test
    public void testPermissaoUsuarioSimples() {
        Usuario usuario = new Usuario("usuario","123","Usuario");
        assertFalse(permissao.podeCadastrar(usuario));
        assertFalse(permissao.podeConsultar(usuario));
    }

    @Test
    public void testTipoInvalido() {
        Usuario invalido = new Usuario("teste","123","Outro");
        IllegalArgumentException assertThrows = assertThrows(IllegalArgumentException.class, () -> permissao.podeCadastrar(invalido));
    }
}
