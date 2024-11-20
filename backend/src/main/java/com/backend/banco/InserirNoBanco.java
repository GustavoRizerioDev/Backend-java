package com.backend.banco;

import com.backend.Main;
import com.backend.bucket.BucketServices;
import com.backend.leituraExcel.Energia;
import com.backend.leituraExcel.LeitorExcel;
import com.backend.notification.SlackLogs;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InserirNoBanco {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    Conexao conexao = new Conexao();
    BucketServices s3Service = new BucketServices();
    JdbcTemplate con = conexao.getConnection();
    SlackLogs slackLogs = new SlackLogs("Enviando Mensagem:");
    StringBuilder logBuilder = new StringBuilder(); // Para consolidar os logs gerados

    public void inserirDados() throws IOException {
        InputStream excelArchive = s3Service.getExcelFileFromS3("vertex-bucket-xls", "qlikview-consumo-de-energia-2024.xlsx");
        String nomeArquivo = "qlikview-consumo-de-energia-2024.xlsx";
        int ano = Integer.parseInt(nomeArquivo.replaceAll("\\D", "").substring(0, 4));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LeitorExcel leitorExcel = new LeitorExcel();
        List<Energia> energiaExtraida = leitorExcel.extrairEnergia(nomeArquivo, excelArchive, ano);

        for (Energia energia : energiaExtraida) {
            String checkSql = "SELECT COUNT(*) FROM Energia WHERE mes = ? AND ano = ? AND local = ?";
            Integer count = con.queryForObject(checkSql, Integer.class, energia.getMes(), energia.getAno(), energia.getLocal());

            if (count != null && count == 0) {
                String sql = "INSERT INTO Energia (gastoEnergetico, gastoEmReais, mes, local, ano, fk_empresa) VALUES (?, ?, ?, ?, ?, ?)";
                try {
                    con.update(sql, energia.getKwh(), energia.getGasto(), energia.getMes(), energia.getLocal(), energia.getAno(), 1);
                    String dataHora = LocalDateTime.now().format(formatter);
                    String successMessage = String.format("[%s] Inserção bem-sucedida para: %s%n", dataHora, energia);
                    String successMessageColored = String.format("\u001B[32m[%s] Inserção bem-sucedida para: %s\u001B[0m", dataHora, energia);
                    slackLogs.sendNotification(successMessage);

                    System.out.println(successMessageColored);
                    logBuilder.append(successMessage);

                } catch (Exception e) {
                    String dataHora = LocalDateTime.now().format(formatter);
                    String errorMessage = String.format("[%s] Falha ao inserir energia: %s%n", dataHora, energia);

                    logBuilder.append(errorMessage);
                    logger.log(Level.SEVERE, errorMessage, e);
                }
            } else {
                String dataHora = LocalDateTime.now().format(formatter);
                String warnMessage = String.format("[%s] Registro já existente para: %s%n", dataHora, energia);
                String warnMessageColored = String.format("\u001B[33m[%s] Registro já existente para: %s\u001B[0m", dataHora, energia);

                System.out.println(warnMessageColored);
                logBuilder.append(warnMessage);
            }
        }

        slackLogs.sendNotification(logBuilder.toString());
        s3Service.enviarArquivo(logBuilder.toString());
    }
}
