package com.backend.notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackClients extends Slack {

    private final String slackToken;
    private final String channelId;

    public SlackClients(String mensagem, String slackToken, String channelId) {
        super(mensagem);
        this.slackToken = slackToken;
        this.channelId = channelId;
    }

    public void sendNotification(String mensagem) {
        HttpURLConnection connection = null;

        try {
            // Configura a conexão HTTP
            URL url = new URL("https://slack.com/api/chat.postMessage");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + slackToken);
            connection.setDoOutput(true);

            // Log do token e do canal para depuração
            System.out.println("Usando Token: " + slackToken);
            System.out.println("Enviando para o canal: " + channelId);

            // Monta o payload da requisição
            String payload = String.format(
                    "{\"channel\": \"%s\", \"text\": \"%s\"}",
                    channelId,
                    mensagem
            );

            // Envia os dados
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verifica a resposta do servidor
            int responseCode = connection.getResponseCode();
            System.out.printf("Código de resposta do Slack: %d%n", responseCode);

            if (responseCode == 200) {
                // Mensagem enviada com sucesso
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    System.out.println("Mensagem enviada com sucesso. Resposta: " + response);
                }
            } else {
                // Erro ao enviar mensagem
                System.out.println("Erro ao enviar mensagem. Código de resposta: " + responseCode);
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.out.println("Detalhes do erro: " + errorResponse);
                }
            }
        } catch (Exception e) {
            // Trata exceções gerais
            System.err.println("Erro ao enviar notificação para o Slack: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Certifique-se de desconectar a conexão
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
