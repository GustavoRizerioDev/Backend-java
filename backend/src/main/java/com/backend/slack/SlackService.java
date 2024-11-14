package com.backend.slack;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackService {

    private final String webhookUrl;

    // Construtor para configurar a URL do webhook
    public SlackService(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    // Metodo para enviar uma notificação personalizada ao Slack
    public void sendNotification(String message) {
        try {
            // Cria a URL de conexão
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // Cria o corpo JSON com a mensagem
            String payload = "{\"text\": \"" + message + "\"}";

            // Envia a mensagem como um byte stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lê a resposta do Slack
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


}
