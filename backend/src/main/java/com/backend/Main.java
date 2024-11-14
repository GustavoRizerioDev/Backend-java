package com.backend;

import com.backend.banco.Conexao;
import com.backend.banco.CriacaoDeTabelas;
import com.backend.banco.InserirNoBanco;
import com.backend.bucket.BucketServices;
import com.backend.slack.SlackService;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
        String webhookUrl = "https://hooks.slack.com/services/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";

        BucketServices bucketServices = new BucketServices();
        Conexao con = new Conexao();
        CriacaoDeTabelas criar = new CriacaoDeTabelas();
        InserirNoBanco inserir = new InserirNoBanco();

        //bucketServices.baixarArquivoLocal();
        criar.criarTabelas();
        inserir.inserirDados();

        // SlackService slack = new SlackService(webhookUrl);
        // slack.sendNotification("Olá, lucas cancela! Esta é uma mensagem de teste do meu projeto.");
    }
}
