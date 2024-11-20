package com.backend.banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {

    private final DataSource dataSource;
    public Conexao(){

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://container-bd-vertex:3306/Vertex");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("urubu100");

        this.dataSource = basicDataSource;

    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}