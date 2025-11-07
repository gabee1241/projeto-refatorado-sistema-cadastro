package com.mycompany.projeto_integrador.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    // Server connection used to create the database if it doesn't exist
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // <- change
    private static final String PASSWORD = "Maeepai1234567!@!"; // <- change

    public static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }

        // Create database if not exists
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS sistema_cadastro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        } catch (Exception e) {
            throw new RuntimeException("Error creating database sistema_cadastro", e);
        }

        // Create tables if not exists
        try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement()) {
            String usuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "senha VARCHAR(100) NOT NULL, " +
                    "tipo_usuario VARCHAR(20) NOT NULL, " +
                    "tipo VARCHAR(50)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

            String registros = "CREATE TABLE IF NOT EXISTS registros (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "identificacao VARCHAR(100) NOT NULL, " +
                    "idade INT NOT NULL, " +
                    "curso_departamento VARCHAR(50)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

            st.execute(usuario);
            st.execute(registros);
        } catch (Exception e) {
            throw new RuntimeException("Error creating tables", e);
        }
    }
}
