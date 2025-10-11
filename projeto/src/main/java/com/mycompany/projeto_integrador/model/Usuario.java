package com.mycompany.projeto_integrador.model;

public class Usuario {
    private int id;
    private String nome;
    private String senha;
    private String tipoUsuario;
    private String tipo;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", tipoUsuario='" + tipoUsuario + '\'' + ", tipo='" + tipo + '\'' + '}';
    }
}
