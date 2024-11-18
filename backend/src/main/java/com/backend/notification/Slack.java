package com.backend.notification;

public abstract class Slack {
    protected String webhookUrl;
    protected String mensagem;

    public Slack(String mensagem) {
        this.mensagem = mensagem;
    }

    public abstract void sendNotification(String mensagem);
}
