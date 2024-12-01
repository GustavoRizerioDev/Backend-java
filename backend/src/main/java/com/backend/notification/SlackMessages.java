package com.backend.notification;

import com.backend.banco.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SlackMessages {
    private final JdbcTemplate jdbcTemplate;
    private final String webhookUrl;

    public SlackMessages(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.jdbcTemplate = new Conexao().getConnection();
    }

    public String generateConsumoElevadoMessage() {
        String consumoAltoQuery = """
            SELECT e.local, e.mes, e.ano, e.gastoEnergetico AS kwh, e.gastoEmReais AS gasto
            FROM Energia e
            WHERE e.gastoEnergetico > (SELECT AVG(gastoEnergetico) FROM Energia)
            ORDER BY e.gastoEnergetico DESC LIMIT 5
        """;
        List<Map<String, Object>> consumoAlto = jdbcTemplate.queryForList(consumoAltoQuery);

        StringBuilder message = new StringBuilder();
        String timestamp = getCurrentTimestamp();

        for (Map<String, Object> registro : consumoAlto) {
            message.append(String.format(
                    "⚡ *Consumo elevado detectado!* [%s]\nLocal: %s | Mês/Ano: %s/%s | Consumo: %s kWh | Gasto: R$ %s. Verifique a [dashboard](#).\n",
                    timestamp, registro.get("local"), registro.get("mes"), registro.get("ano"), registro.get("kwh"), registro.get("gasto")
            ));
        }

        String finalMessage = message.toString();
        if (!finalMessage.isEmpty()) {
            sendOnceToSlack("Consumo Elevado", finalMessage);
        }

        return finalMessage;
    }

    public String generateTopLocaisGastoMessage() {
        String query = """
            SELECT e.local, e.mes, e.ano, SUM(e.gastoEnergetico) AS consumoTotal, SUM(e.gastoEmReais) AS gastoTotal
            FROM Energia e
            GROUP BY e.local, e.mes, e.ano
            ORDER BY consumoTotal DESC LIMIT 5
        """;

        List<Map<String, Object>> topLocais = jdbcTemplate.queryForList(query);

        StringBuilder message = new StringBuilder();
        String timestamp = getCurrentTimestamp();

        for (Map<String, Object> registro : topLocais) {
            message.append(String.format(
                    "💰 *Maior Gasto de Energia!* [%s]\nLocal: %s | Mês/Ano: %s/%s | Consumo: %s kWh | Gasto: R$ %s. Verifique a [dashboard](#).\n",
                    timestamp, registro.get("local"), registro.get("mes"), registro.get("ano"), registro.get("consumoTotal"), registro.get("gastoTotal")
            ));
        }

        String finalMessage = message.toString();
        if (!finalMessage.isEmpty()) {
            sendOnceToSlack("Top Locais de Gasto", finalMessage);
        }

        return finalMessage;
    }

    public String generateAlertasDeConsumoAcimaDaMetaMessage() {
        String query = """
            SELECT e.local, e.mes, e.ano, e.gastoEnergetico AS consumoKwh, m.gastoEnergetico AS metaKwh
            FROM Energia e
            JOIN Metas m ON e.mes = m.mes AND e.fk_empresa = m.fk_empresa
            WHERE e.gastoEnergetico > m.gastoEnergetico
            ORDER BY e.gastoEnergetico DESC LIMIT 3
        """;

        List<Map<String, Object>> alertas = jdbcTemplate.queryForList(query);

        StringBuilder message = new StringBuilder();
        String timestamp = getCurrentTimestamp();

        for (Map<String, Object> alerta : alertas) {
            message.append(String.format(
                    "⚠️ *Alerta de Consumo Acima da Meta!* [%s]\nLocal: %s | Mês/Ano: %s/%s | Consumo: %s kWh | Meta: %s kWh. Verifique a [dashboard](#).\n",
                    timestamp, alerta.get("local"), alerta.get("mes"), alerta.get("ano"), alerta.get("consumoKwh"), alerta.get("metaKwh")
            ));
        }

        String finalMessage = message.toString();
        if (!finalMessage.isEmpty()) {
            sendOnceToSlack("Alertas de Consumo Acima da Meta", finalMessage);
        }

        return finalMessage;
    }

    public String generateResumoMensalMessage() {
        String resumoQuery = """
            SELECT SUM(gastoEnergetico) AS totalKwh, SUM(gastoEmReais) AS totalGasto
            FROM Energia
            WHERE mes = ? AND ano = ?
        """;

        int mesAtual = LocalDateTime.now().getMonthValue();
        int anoAtual = LocalDateTime.now().getYear();
        String mesAnteriorNome = Month.of((mesAtual == 1 ? 12 : mesAtual - 2))
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        int anoConsulta = (mesAtual == 1) ? anoAtual - 1 : anoAtual;

        Map<String, Object> resumoMensal = jdbcTemplate.queryForMap(resumoQuery, mesAnteriorNome, anoConsulta);

        String timestamp = getCurrentTimestamp();
        String finalMessage;
        if (resumoMensal == null || resumoMensal.get("totalKwh") == null || resumoMensal.get("totalGasto") == null) {
            finalMessage = String.format(
                    "📊 *Resumo do Mês*: [%s]\nNão há dados disponíveis para o consumo e gasto deste mês. Verifique os registros.", timestamp
            );
        } else {
            finalMessage = String.format(
                    "📊 *Resumo do Mês*: [%s]\nNo mês de %s, o consumo total foi de %s kWh e o gasto total foi de R$ %s. Confira os detalhes na [dashboard](#).",
                    timestamp, mesAnteriorNome, resumoMensal.get("totalKwh"), resumoMensal.get("totalGasto")
            );
        }

        if (!finalMessage.isEmpty()) {
            sendOnceToSlack("Resumo Mensal", finalMessage);
        }

        return finalMessage;
    }

    private void sendOnceToSlack(String notificationType, String message) {
        sendToSlack(message);
        saveToDatabase(notificationType, message);
    }

    private void sendToSlack(String message) {
        try {
            if (message == null || message.isEmpty()) {
                return;
            }

            String payload = "{\"text\": \"" + message + "\"}";
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Mensagem enviada com sucesso ao Slack.");
            } else {
                System.err.println("Erro ao enviar mensagem ao Slack. Código de resposta: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação para o Slack: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveToDatabase(String nome, String descricao) {
        String insertQuery = """
            INSERT INTO Notificacao (nome, descricao, data) 
            VALUES (?, ?, ?)
        """;

        jdbcTemplate.update(insertQuery, nome, descricao, LocalDateTime.now());
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.now().format(formatter);
    }
}