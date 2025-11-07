package com.mycompany.projeto_integrador.app;

import com.mycompany.projeto_integrador.dao.UsuarioDAOJdbc;
import com.mycompany.projeto_integrador.exception.DaoException;
import com.mycompany.projeto_integrador.model.Usuario;
import com.mycompany.projeto_integrador.util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe de teste rápido para verificar se a conexão e os métodos DAO estão funcionando.
 * Executa no console — não abre interface gráfica.
 */
public class MainJdbc {
    public static void main(String[] args) {
        System.out.println("=== TESTE JDBC - SISTEMA CADASTRO ===");

        // 1️⃣ Testar conexão
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[OK] Conexão com o banco de dados estabelecida!");
            } else {
                System.err.println("[ERRO] Falha ao conectar com o banco de dados.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("[ERRO] Não foi possível conectar ao banco: " + e.getMessage());
            return;
        }

        // 2️⃣ Testar inserção de usuário
        UsuarioDAOJdbc dao = new UsuarioDAOJdbc();
        Usuario usuario = new Usuario();
        usuario.setNome("Operador");
        usuario.setSenha("1234");
        usuario.setTipoUsuario("Operador"); // Pode ser Administrador, Operador ou Usuario

        try {
            dao.salvar(usuario);
            System.out.println("[OK] Usuário cadastrado com sucesso!");
        } catch (DaoException e) {
            System.err.println("[ERRO] Falha ao salvar usuário: " + e.getMessage());
        }

        // 3️⃣ Listar todos os usuários
        try {
            List<Usuario> usuarios = dao.listar();
            System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
            for (Usuario u : usuarios) {
                System.out.printf("ID: %d | Nome: %s | Tipo: %s%n",
                        u.getId(), u.getNome(), u.getTipoUsuario());
            }
        } catch (DaoException e) {
            System.err.println("[ERRO] Falha ao listar usuários: " + e.getMessage());
        }

        // 4️⃣ Testar autenticação
        try {
            Usuario uLogin = dao.buscarUsuario("Operador", "1234");
            if (uLogin != null) {
                System.out.println("\n[OK] Login realizado com sucesso!");
                System.out.println("Usuário autenticado: " + uLogin.getNome() + " | Tipo: " + uLogin.getTipoUsuario());
            } else {
                System.out.println("\n[ERRO] Usuário ou senha incorretos!");
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao autenticar: " + e.getMessage());
        }

        System.out.println("\n=== FIM DO TESTE ===");
    }
}
