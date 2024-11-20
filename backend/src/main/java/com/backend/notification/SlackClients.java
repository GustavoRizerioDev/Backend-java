package com.backend.notification;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackClients extends Slack {


    public SlackClients(String mensagem) {

        super(mensagem);
        this.webhookUrl = "https://hooks.slack.com/services/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";
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
