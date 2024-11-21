package com.backend.notification;

import com.backend.banco.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SlackMessages {
    private final JdbcTemplate jdbcTemplate;
    private final String slackToken;
    private final String channelId; // Canal para envio das mensagens

    public SlackMessages(String slackToken, String channelId) {
        this.slackToken = slackToken;
        this.channelId = channelId;
        this.jdbcTemplate = new Conexao().getConnection();
    }

    private void sendToSlack(String message) {
        try {
            URL url = new URL("https://slack.com/api/chat.postMessage");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + slackToken);
            connection.setDoOutput(true);

            String payload = String.format("{\"channel\": \"%s\", \"text\": \"%s\"}", channelId, message);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
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

    public String generateConsumoElevadoMessage() {
        String consumoAltoQuery = """
            SELECT e.local, e.mes, e.ano, e.gastoEnergetico AS kwh, e.gastoEmReais AS gasto
            FROM Energia e
            WHERE e.gastoEnergetico > (SELECT AVG(gastoEnergetico) FROM Energia)
            ORDER BY e.gastoEnergetico DESC LIMIT 5
        """;

        List<Map<String, Object>> consumoAlto = jdbcTemplate.queryForList(consumoAltoQuery);

        StringBuilder message = new StringBuilder();

        for (Map<String, Object> registro : consumoAlto) {
            message.append(String.format(
                    "⚡ *Consumo elevado detectado!* Local: %s | Mês/Ano: %s/%s | Consumo: %s kWh | Gasto: R$ %s. Verifique a [dashboard](#).\n",
                    registro.get("local"), registro.get("mes"), registro.get("ano"), registro.get("kwh"), registro.get("gasto")
            ));
        }

        if (message.length() > 0) {
            sendToSlack(message.toString());
        }

        return message.toString();
    }

    public String generateTopLocaisGastoMessage() {
        String query = """
        SELECT e.local, SUM(e.gastoEnergetico) AS consumoTotal, SUM(e.gastoEmReais) AS gastoTotal
        FROM Energia e
        GROUP BY e.local
        ORDER BY gastoTotal DESC
        LIMIT 5
    """;

        List<Map<String, Object>> topLocais = jdbcTemplate.queryForList(query);

        StringBuilder message = new StringBuilder();
        for (Map<String, Object> registro : topLocais) {
            message.append(String.format(
                    "💰 *Maior Gasto de Energia!* Local: %s | Consumo: %s kWh | Gasto: R$ %s. Verifique a [dashboard](#).\n",
                    registro.get("local"), registro.get("consumoTotal"), registro.get("gastoTotal")
            ));
        }

        if (message.length() > 0) {
            sendToSlack(message.toString());
        }

        return message.toString();
    }

    public String generateAlertasDeConsumoAcimaDaMetaMessage() {
        String query = """
    SELECT a.nome, e.mes, e.ano, e.gastoEnergetico AS consumoKwh, m.gastoEnergetico AS metaKwh
    FROM Energia e
    JOIN Metas m ON e.mes = m.mes AND e.fk_empresa = m.fk_empresa
    JOIN Alertas a ON e.gastoEnergetico > m.gastoEnergetico
    ORDER BY e.gastoEnergetico DESC
    LIMIT 3
    """;

        List<Map<String, Object>> alertas = jdbcTemplate.queryForList(query);

        StringBuilder message = new StringBuilder();
        for (Map<String, Object> alerta : alertas) {
            message.append(String.format(
                    "⚠️ *Alerta de Consumo Acima da Meta!* Alerta: %s | Mês: %s/%s | Consumo: %s kWh | Meta: %s kWh. Verifique a [dashboard](#).\n",
                    alerta.get("nome"), alerta.get("mes"), alerta.get("ano"), alerta.get("consumoKwh"), alerta.get("metaKwh")
            ));
        }

        if (message.length() > 0) {
            sendToSlack(message.toString());
        }

        return message.length() > 0 ? message.toString() : "Nenhum alerta de consumo acima da meta encontrado.";
    }

    public String generateResumoMensalMessage() {
        String resumoQuery = """
        SELECT SUM(gastoEnergetico) AS totalKwh, SUM(gastoEmReais) AS totalGasto
        FROM Energia
        WHERE mes = ? AND ano = ?
    """;

        int mesAtual = LocalDateTime.now().getMonthValue();
        int anoAtual = LocalDateTime.now().getYear();

        Map<String, Object> resumoMensal = jdbcTemplate.queryForMap(resumoQuery, String.valueOf(mesAtual), anoAtual);

        if (resumoMensal == null || resumoMensal.get("totalKwh") == null || resumoMensal.get("totalGasto") == null) {
            return "📊 *Resumo do Mês*: Não há dados disponíveis para o consumo e gasto deste mês. Verifique os registros.";
        }

        String message = String.format(
                "📊 *Resumo do Mês*: Consumo total: %s kWh | Gasto total: R$ %s. Confira os detalhes na [dashboard](#).",
                resumoMensal.get("totalKwh"), resumoMensal.get("totalGasto")
        );

        sendToSlack(message);
        return message;
    }
}
