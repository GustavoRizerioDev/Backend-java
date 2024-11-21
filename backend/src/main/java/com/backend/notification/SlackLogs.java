package com.backend.notification;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SlackLogs extends Slack {

    private final String slackToken;
    private final String channelId;

    public SlackLogs(String mensagem, String slackToken, String channelId) {
        super(mensagem);
        this.slackToken = slackToken;
        this.channelId = channelId;
    }

    @Override
    public void sendNotification(String mensagem) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("https://slack.com/api/chat.postMessage"); // Endpoint oficial da Slack API
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + slackToken); // Autorização com token
            connection.setDoOutput(true);

            String payload = buildPayload(mensagem);

            // Envio do payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Processamento da resposta
            int responseCode = connection.getResponseCode();
            handleResponse(responseCode, connection);

        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem ao Slack: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildPayload(String message) {
        // Constrói o payload no formato JSON para o Slack
        return String.format("{\"channel\": \"%s\", \"text\": \"%s\"}", channelId, escapeJson(message));
    }

    private void handleResponse(int responseCode, HttpURLConnection connection) throws IOException {
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Log enviado com sucesso ao Slack.");
        } else {
            System.err.println("Erro ao enviar mensagem ao Slack. Código de resposta: " + responseCode);

            // Leitura opcional do corpo da resposta de erro, se disponível
            try (InputStream errorStream = connection.getErrorStream()) {
                if (errorStream != null) {
                    String errorMessage = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    System.err.println("Detalhes do erro: " + errorMessage);
                }
            }
        }
    }

    private String escapeJson(String message) {
        // Escapa caracteres especiais para JSON
        return message.replace("\"", "\\\"");
    }

    public String formatLogMessage(String log) {
        // Método para retornar a mensagem formatada
        return String.format("📄 *Log*: %s", escapeJson(log));
    }
}
