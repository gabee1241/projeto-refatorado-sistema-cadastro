-- ==================== SCRIPT COMPLETO DO BANCO DE DADOS ====================
-- Sistema de Cadastro de Alunos e Professores
-- MySQL 8.0+
-- ===========================================================================

-- Criar o banco de dados (se não existir)
CREATE DATABASE IF NOT EXISTS cadastro_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Selecionar o banco de dados
USE cadastro_db;

-- ==================== REMOVER TABELAS EXISTENTES ====================
-- Cuidado: Isso apagará todos os dados!

DROP TABLE IF EXISTS alunos;
DROP TABLE IF EXISTS professores;
DROP TABLE IF EXISTS usuarios;

-- ==================== CRIAR TABELAS ====================

-- Tabela de Usuários
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMIN', 'OPERADOR', 'USUARIO') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_perfil (perfil)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Alunos
CREATE TABLE alunos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    rg VARCHAR(20) NOT NULL UNIQUE,
    curso VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nome (nome),
    INDEX idx_rg (rg),
    INDEX idx_curso (curso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Professores
CREATE TABLE professores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    rg VARCHAR(20) NOT NULL UNIQUE,
    departamento VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nome (nome),
    INDEX idx_rg (rg),
    INDEX idx_departamento (departamento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO usuarios (username, senha, perfil) VALUES
('admin', 'admin', 'ADMIN'),
('operador', 'operador', 'OPERADOR'),
('usuario', 'usuario', 'USUARIO');