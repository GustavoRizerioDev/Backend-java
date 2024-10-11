package com.backend.banco.tabelas;

public class Contato {

    Integer idContato;
    String Nome;
    String Email;
    Integer Telefone;

    public Contato(){

    }

    public Contato(Integer idContato, String nome, String email, Integer telefone) {
        this.idContato = idContato;
        Nome = nome;
        Email = email;
        Telefone = telefone;
    }

    public Integer getIdContato() {
        return idContato;
    }

    public void setIdContato(Integer idContato) {
        this.idContato = idContato;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getTelefone() {
        return Telefone;
    }

    public void setTelefone(Integer telefone) {
        Telefone = telefone;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "idContato=" + idContato +
                ", Nome='" + Nome + '\'' +
                ", Email='" + Email + '\'' +
                ", Telefone=" + Telefone +
                '}';
    }
}
