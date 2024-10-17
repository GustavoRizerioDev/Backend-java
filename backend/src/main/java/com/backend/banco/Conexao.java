package com.backend.banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {

    private final DataSource dataSource;

    public Conexao(){

    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    basicDataSource.setUrl(System.getenv("DB_HOST"));
    basicDataSource.setUsername(System.getenv("DB_USER"));
    basicDataSource.setPassword(System.getenv("DB_PASSWORD"));

    this.dataSource = basicDataSource;

    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}
