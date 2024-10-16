package main.java.com.backend;

import main.java.com.backend.banco.Conexao;
import main.java.com.backend.banco.CriacaoDeTabelas;
import main.java.com.backend.banco.InserirNoBanco;
import main.java.com.backend.bucket.BucketServices;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

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
