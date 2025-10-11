package com.mycompany.projeto_integrador.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Configure your MySQL connection here
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_cadastro?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // <- change to your user
    private static final String PASSWORD = "Maeepai1234567!@!"; // <- change to your password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
