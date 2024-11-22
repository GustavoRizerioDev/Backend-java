package com.backend;

import com.backend.banco.Conexao;
import com.backend.banco.CriacaoDeTabelas;
import com.backend.banco.InserirNoBanco;
import com.backend.bucket.BucketServices;
import com.backend.notification.SlackClients;
import com.backend.notification.SlackMessages;

import java.io.IOException;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
        String slackToken = System.getenv("SLACK_TOKEN");
        String channelId = "#vertex";

        BucketServices bucketServices = new BucketServices();
        Conexao con = new Conexao();
        CriacaoDeTabelas criar = new CriacaoDeTabelas();
        InserirNoBanco inserir = new InserirNoBanco();

        bucketServices.listarBucket();
        criar.criarTabelas();
        inserir.inserirDados();

        System.out.println("Token do Slack: " + slackToken);
        SlackMessages notifier = new SlackMessages(slackToken, channelId);

        String consumoElevadoMessage = notifier.generateConsumoElevadoMessage();
        String resumoSemanalMessage = notifier.generateResumoMensalMessage();
        String topLocaisGastoMessage = notifier.generateTopLocaisGastoMessage();
        String alertasDeConsumoAcimaDaMetaMessage = notifier.generateAlertasDeConsumoAcimaDaMetaMessage();

        SlackClients messageSender = new SlackClients("Enviando mensagem:", slackToken, channelId);

        messageSender.sendNotification(consumoElevadoMessage);
        messageSender.sendNotification(topLocaisGastoMessage);
        messageSender.sendNotification(alertasDeConsumoAcimaDaMetaMessage);
        messageSender.sendNotification(resumoSemanalMessage);

    }
}
