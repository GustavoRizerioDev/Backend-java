package com.backend.banco;

import org.springframework.jdbc.core.JdbcTemplate;

public class CriacaoDeTabelas {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConnection();

        public void criarTabelas(){
            con.execute("""
                CREATE TABLE IF NOT EXISTS Cargos (
                    idCargos INT AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    temPermissaoAdm CHAR(3) NOT NULL,
                    PRIMARY KEY (idCargos)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Genero (
                    idGenero INT AUTO_INCREMENT,
                    nomeGenero VARCHAR(45) NOT NULL,
                    PRIMARY KEY (idGenero)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Empresa (
                    idEmpresa INT AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    cnpj CHAR(14) NOT NULL,
                    endereco VARCHAR(255) NOT NULL,
                    telefone VARCHAR(20) NOT NULL,
                    PRIMARY KEY (idEmpresa)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Usuario (
                    idUsuario INT AUTO_INCREMENT,
                    nome VARCHAR(255) NOT NULL,
                    senha VARCHAR(40) NOT NULL,
                    email VARCHAR(256) NOT NULL,
                    fk_cargos INT NOT NULL,
                    fk_idGenero INT NOT NULL,
                    fk_idEmpresa INT NOT NULL,
                    PRIMARY KEY (idUsuario),
                    FOREIGN KEY (fk_cargos) REFERENCES Cargos (idCargos),
                    FOREIGN KEY (fk_idGenero) REFERENCES Genero (idGenero),
                    FOREIGN KEY (fk_idEmpresa) REFERENCES Empresa (idEmpresa)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Energia (
                    idEnergia INT AUTO_INCREMENT,
                    gastoEnergetico INT NOT NULL,
                    gastoEmReais INT NOT NULL,
                    mes VARCHAR(10) NOT NULL,
                    local VARCHAR(60) NOT NULL,
                    ano INT NOT NULL,
                    fk_empresa INT NOT NULL,
                    PRIMARY KEY (idEnergia),
                    FOREIGN KEY (fk_empresa) REFERENCES Empresa (idEmpresa)
                );""");

            con.execute("""
               CREATE TABLE IF NOT EXISTS Metas (
                    idMetas INT AUTO_INCREMENT,
                    gastoEnergetico INT NOT NULL,
                    gastoEmReais INT NOT NULL,
                    mes VARCHAR(10) NOT NULL,
                    fk_empresa INT NOT NULL,
                    PRIMARY KEY (idMetas),
                    FOREIGN KEY (fk_empresa) REFERENCES Empresa (idEmpresa)
               );""");

            con.execute("""
               CREATE TABLE IF NOT EXISTS Metas (
                    idMetas INT AUTO_INCREMENT,
                    gastoEnergetico INT NOT NULL,
                    gastoEmReais INT NOT NULL,
                    mes VARCHAR(10) NOT NULL,
                    fk_empresa INT NOT NULL,
                    PRIMARY KEY (idMetas),
                    FOREIGN KEY (fk_empresa) REFERENCES Empresa (idEmpresa)
               );""");

            con.execute("""
               CREATE TABLE IF NOT EXISTS Logs (
                    idlog INT AUTO_INCREMENT,
                    data TIMESTAMP NOT NULL,
                    classe VARCHAR(255) NOT NULL,
                    tipo VARCHAR(45) NOT NULL,
                    descricao VARCHAR(255) NOT NULL,
                    PRIMARY KEY (idlog)
               );""");

            con.execute("""
               CREATE TABLE IF NOT EXISTS Alertas (
                    idNotificacao INT AUTO_INCREMENT,
                    nome VARCHAR(100) NOT NULL,
                    descricao VARCHAR(255) NOT NULL,
                    tipo VARCHAR(45) NOT NULL,
                    data TIMESTAMP NOT NULL,
                    PRIMARY KEY (idNotificacao)
               );""");
        }

}
