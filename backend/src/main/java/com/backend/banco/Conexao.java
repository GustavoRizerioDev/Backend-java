package main.java.com.backend.banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {

    private final JdbcTemplate conexaoDoBanco;

    public Conexao(){


    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setDriverClassName(System.getenv("DB_DRIVER_NAME"));
    basicDataSource.setUrl(System.getenv("DB_HOST"));
    basicDataSource.setUsername(System.getenv("DB_USER"));
    basicDataSource.setPassword(System.getenv("DB_PASSWORD"));


    conexaoDoBanco = new JdbcTemplate(basicDataSource);

    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
