package com.backend.banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {


    // Ambiente Em Produção
    private final DataSource dataSource;
    public Conexao(){

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/vertex");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("urubu100");

        this.dataSource = basicDataSource;

    }


    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }

    /* Ambiente Local

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
    */

}
