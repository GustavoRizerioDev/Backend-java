package com.backend.banco.tabelas;

public class Local {

    Integer idLocal;
    String Nome;

    public Local(){

    }

    public Local(Integer idLocal, String nome) {
        this.idLocal = idLocal;
        this.Nome = nome;
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    @Override
    public String toString() {
        return "Local{" +
                "idLocal=" + idLocal +
                ", Nome='" + Nome + '\'' +
                '}';
    }
}
