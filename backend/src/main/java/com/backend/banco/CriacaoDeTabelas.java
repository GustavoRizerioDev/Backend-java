package main.java.com.backend.banco;

import org.springframework.jdbc.core.JdbcTemplate;

public class CriacaoDeTabelas {
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();

        public void criarTabelas(){
            con.execute("""
                CREATE TABLE IF NOT EXISTS Cargos (
                  idCargos INT NOT NULL auto_increment,
                  Nome VARCHAR(50) NOT NULL,
                  PRIMARY KEY (idCargos)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Usuario (
                    idUsuario INT NOT NULL auto_increment,
                    Nome VARCHAR(100) NOT NULL,
                    Senha VARCHAR(255) NOT NULL,
                    Email VARCHAR(100) NOT NULL,
                    Sexo ENUM('Masculino', 'Feminino', 'Outro') NOT NULL,
                    fk_cargos INT NOT NULL,
                    PRIMARY KEY (idUsuario),
                    CONSTRAINT fk_Usuario_Cargos
                      FOREIGN KEY (fk_cargos)
                      REFERENCES Cargos (idCargos)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS IA (
                  idIA INT NOT NULL auto_increment,
                  Prompt TEXT NOT NULL,
                  PRIMARY KEY (idIA)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Notificacao (
                  idNotificacao INT NOT NULL auto_increment,
                  Nome VARCHAR(100) NOT NULL,
                  Descricao TEXT NOT NULL,
                  PRIMARY KEY (idNotificacao)
                );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Contato (
                    idContato INT NOT NULL auto_increment,
                    Nome VARCHAR(45) NOT NULL,
                    Email VARCHAR(45) NULL,
                    Telefone VARCHAR(45) NULL,
                    PRIMARY KEY (idContato)
                  );""");

            con.execute("""
                CREATE TABLE IF NOT EXISTS Logs(
                   idLog INT NOT NULL auto_increment,
                   descricao VARCHAR(1000) NOT NULL,
                   data DATETIME,
                   PRIMARY KEY(idLog)
            );
            """);

            con.execute("""
                CREATE TABLE IF NOT EXISTS Energia (
                  idEnergia INT NOT NULL auto_increment,
                  Kwh INT NOT NULL,
                  Gasto INT NOT NULL,
                  Mes VARCHAR(20) NOT NULL,
                  local VARCHAR(255) NOT NULL,
                  Ano INT NOT NULL,
                  PRIMARY KEY (idEnergia)
            );
            """);
        }

}
