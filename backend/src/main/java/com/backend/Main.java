package com.backend;

import com.backend.banco.Conexao;
import com.backend.banco.CriacaoDeTabelas;
import com.backend.banco.InserirNoBanco;
import com.backend.bucket.BucketServices;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Diretório de trabalho atual: " + System.getProperty("user.dir"));
        BucketServices bucketServices = new BucketServices();
        Conexao con = new Conexao();
        CriacaoDeTabelas criar = new CriacaoDeTabelas();
        InserirNoBanco inserir = new InserirNoBanco();

        bucketServices.criarBucket();
        bucketServices.listarBucket();
        bucketServices.baixarArquivoLocal();

        criar.criarTabelas();
        inserir.inserirDados();
    }
}
