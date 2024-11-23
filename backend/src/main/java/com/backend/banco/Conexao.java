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

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalArgumentException("As variáveis de ambiente DB_URL, DB_USER ou DB_PASSWORD não estão configuradas.");
        }

        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(dbUser);
        basicDataSource.setPassword(dbPassword);

        this.dataSource = basicDataSource;
    }


    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}