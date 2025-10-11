package com.mycompany.projeto_integrador.model;

public class Registro {
    private int id;
    private String nome;
    private String identificacao;
    private int idade;
    private String cursoDepartamento;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getCursoDepartamento() { return cursoDepartamento; }
    public void setCursoDepartamento(String cursoDepartamento) { this.cursoDepartamento = cursoDepartamento; }

    @Override
    public String toString() {
        return "Registro{" + "id=" + id + ", nome='" + nome + '\'' + ", identificacao='" + identificacao + '\'' + ", idade=" + idade + ", cursoDepartamento='" + cursoDepartamento + '\'' + '}';
    }
}
