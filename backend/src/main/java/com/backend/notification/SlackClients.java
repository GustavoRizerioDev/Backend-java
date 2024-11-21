package com.backend.notification;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackClients {

    private final String slackToken;
    private final String channelId;

    public SlackClients(String slackToken, String channelId) {
        this.slackToken = slackToken;
        this.channelId = channelId;
    }

    public void sendNotification(String mensagem) {
        try {
            URL url = new URL("https://slack.com/api/chat.postMessage");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + slackToken);
            connection.setDoOutput(true);

            // Payload com a mensagem e o canal
            String payload = String.format(
                    "{\"channel\": \"%s\", \"text\": \"%s\"}",
                    channelId,
                    mensagem
            );

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Mensagem enviada com sucesso ao Slack.");
            } else {
                System.out.printf("Erro ao enviar mensagem ao Slack. Código de resposta: %d%n", responseCode);
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação para o Slack: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
