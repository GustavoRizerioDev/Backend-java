package com.backend.banco.tabelas;

public class Cargos {

    private Integer id;
    private String nome;

    public Cargos(){

    }

    public Cargos(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Cargos{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
