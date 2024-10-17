package com.backend.banco.tabelas;

import java.sql.Timestamp;

public class Logs {
    Integer idLog;
    Timestamp Data;
    String Classe;
    String Tipo;
    String Descricao;

    public Logs() {
    }

    public Logs(Integer idLog, Timestamp data, String classe, String tipo, String descricao) {
        this.idLog = idLog;
        Data = data;
        Classe = classe;
        Tipo = tipo;
        Descricao = descricao;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public Timestamp getData() {
        return Data;
    }

    public void setData(Timestamp data) {
        Data = data;
    }

    public String getClasse() {
        return Classe;
    }

    public void setClasse(String classe) {
        Classe = classe;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "idLog=" + idLog +
                ", Data=" + Data +
                ", Classe='" + Classe + '\'' +
                ", Tipo='" + Tipo + '\'' +
                ", Descricao='" + Descricao + '\'' +
                '}';
    }
}
