package com.backend.banco.tabelas;

public class Usuario {

   private Integer idUsuario;
   private String nome;
   private String senha;
   private String email;
   private Integer fk_cargos;

    public Usuario(){

    }
    public Usuario(Integer idUsuario, String nome, String senha, String email, Integer fk_cargos) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.fk_cargos = fk_cargos;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFk_cargos() {
        return fk_cargos;
    }

    public void setFk_cargos(Integer fk_cargos) {
        this.fk_cargos = fk_cargos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                ", fk_cargos=" + fk_cargos +
                '}';
    }
}
