package com.backend.notification;

public class App {

    public static void main(String[] args) throws Exception {
        SlackClients slackClients = new SlackClients("Teste");
        slackClients.sendNotification("Teste");

        SlackLogs slackLogs = new SlackLogs("Teste");
        slackLogs.sendNotification("Teste");
    }
}
