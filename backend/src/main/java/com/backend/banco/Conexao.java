package com.backend.banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {

    // Ambiente em Produção
    private final DataSource dataSource;

    public Conexao() {
        BasicDataSource basicDataSource = new BasicDataSource();

        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl(dbUrl != null ? dbUrl : "jdbc:mysql://127.0.0.1:3307/Vertex");
        basicDataSource.setUsername(dbUser != null ? dbUser : "root");
        basicDataSource.setPassword(dbPassword != null ? dbPassword : "12345");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}