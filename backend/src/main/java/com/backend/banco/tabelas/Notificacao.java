package com.backend.banco.tabelas;

public class Notificacao {

    Integer idNotificacao;
    String Nome;
    String Descricao;

    public Notificacao(){

    }

    public Notificacao(Integer idNotificacao, String nome, String descricao) {
        this.idNotificacao = idNotificacao;
        Nome = nome;
        Descricao = descricao;
    }

    public Integer getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(Integer idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "idNotificacao=" + idNotificacao +
                ", Nome='" + Nome + '\'' +
                ", Descricao='" + Descricao + '\'' +
                '}';
    }
}
