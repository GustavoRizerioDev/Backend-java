package com.backend.notification;

import com.backend.banco.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class SlackClients extends Slack {
    private final JdbcTemplate jdbcTemplate;

    public SlackClients(String mensagem) {

        super(mensagem);
        this.webhookUrl = "https://hooks.slack.com/services/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";
        this.jdbcTemplate = new Conexao().getConnection();
    }

    public void generateAndSendNotifications() {
        try {
            // 1. Verificar consumo acima da média
            String consumoAltoQuery = """
                SELECT local, mes, ano, kwh, gasto 
                FROM Energia 
                WHERE kwh > (SELECT AVG(kwh) FROM Energia) 
                ORDER BY kwh DESC LIMIT 5
            """;
            List<Map<String, Object>> consumoAlto = jdbcTemplate.queryForList(consumoAltoQuery);

            for (Map<String, Object> registro : consumoAlto) {
                String mensagem = String.format(
                        "⚡ *Consumo elevado detectado!* Local: %s | Mês/Ano: %s/%s | Consumo: %s kWh | Gasto: R$ %s. Verifique a [dashboard](#).",
                        registro.get("local"), registro.get("mes"), registro.get("ano"), registro.get("kwh"), registro.get("gasto")
                );
                sendNotification(mensagem);
            }

            // 2. Alertar sobre metas não atingidas
            String metasQuery = """
                SELECT m.mes, m.kwh AS metaKwh, e.kwh AS consumoKwh, m.gastos AS metaGasto, e.gasto AS consumoGasto
                FROM metas m
                LEFT JOIN Energia e 
                ON m.mes = e.mes AND m.fk_idUsuario = e.fk_empresa
                WHERE e.kwh > m.kwh OR e.gasto > m.gastos
            """;
            List<Map<String, Object>> metasNaoAtingidas = jdbcTemplate.queryForList(metasQuery);

            for (Map<String, Object> meta : metasNaoAtingidas) {
                String mensagem = String.format(
                        "📊 *Meta não atingida!* Mês: %s | Meta Kwh: %s | Consumo Kwh: %s | Meta Gasto: R$ %s | Gasto Real: R$ %s. Verifique a [dashboard](#).",
                        meta.get("mes"), meta.get("metaKwh"), meta.get("consumoKwh"), meta.get("metaGasto"), meta.get("consumoGasto")
                );
                sendNotification(mensagem);
            }

            // 3. Resumo semanal
            String resumoQuery = """
                SELECT SUM(kwh) AS totalKwh, SUM(gasto) AS totalGasto
                FROM Energia
                WHERE EXTRACT(WEEK FROM CURRENT_DATE) = EXTRACT(WEEK FROM data)
            """;
            Map<String, Object> resumoSemanal = jdbcTemplate.queryForMap(resumoQuery);

            String mensagemResumo = String.format(
                    "🗓️ *Resumo Semanal*: Consumo total: %s kWh | Gasto total: R$ %s. Confira os detalhes na [dashboard](#).",
                    resumoSemanal.get("totalKwh"), resumoSemanal.get("totalGasto")
            );
            sendNotification(mensagemResumo);

        } catch (Exception e) {
            e.printStackTrace();
            sendNotification("⚠️ Ocorreu um erro ao gerar notificações. Por favor, verifique os logs do sistema.");
        }
    }

    public void sendNotification(String mensagem) {

        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            String payload = "{\"text\": \"" + mensagem + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            Integer responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Mensagem enviada com sucesso ao Slack.");
            } else {
                System.out.println("Erro ao enviar mensagem ao Slack. Código de resposta: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação para o Slack: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
