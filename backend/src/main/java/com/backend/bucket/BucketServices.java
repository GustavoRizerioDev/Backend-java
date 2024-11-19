
package com.backend.bucket;

import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


import org.slf4j.Logger;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import com.backend.banco.Conexao;

public class BucketServices {
    private static final Logger logger = LoggerFactory.getLogger(BucketServices.class);
    S3Client s3Client = new S3Provider().getS3Client();
    String bucketName = "vertex-bucket-xls"; //cada nome tem que ser único
    JdbcTemplate con = new Conexao().getConnection();

    private void registrarLog(String tipo, String descricao){
        String logSql = "INSERT INTO Logs (data, classe, tipo, descricao) VALUES (?, ?, ?, ?)";
        con.update(logSql, LocalDateTime.now(), "BucketServices", tipo, descricao);
    }

    public void listarBucket(){
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            String listMessage = "Objetos no bucket " + bucketName + ":";
            logger.info(listMessage);
            registrarLog("INFO", listMessage);
            for (S3Object object : objects) {
                logger.info("- " + object.key());
            }
        } catch (S3Exception e) {
            String errorMessage = "Erro ao listar objetos no bucket: " + e.getMessage();
            logger.error(errorMessage);
            registrarLog("ERROR", errorMessage);
        }
    }

    public void enviarArquivo(String message) {
        try {
            // Criando o arquivo localmente
            File file = new File("arquivoS3.txt");
            String conteudo = message;
            Files.writeString(file.toPath(), conteudo);

            String uniqueFileName = "logs-" + UUID.randomUUID() + ".txt";

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            String envioMessage = "Arquivo '" + file.getName() + "' enviado com sucesso para o bucket com o nome: " + uniqueFileName;
            logger.info(envioMessage);
            registrarLog("INFO", envioMessage);

            if (file.delete()) {
                logger.info("Arquivo local '" + file.getName() + "' deletado após upload.");
            }
        } catch (IOException | S3Exception e) {
            String errorMessage = "Erro ao criar ou fazer upload do arquivo: " + e.getMessage();
            logger.error(errorMessage);
            registrarLog("ERROR", errorMessage);
        }
    }

    public InputStream getExcelFileFromS3(String bucketName, String key) {
        S3Provider s3Provider = new S3Provider();
        S3Client s3Client = s3Provider.getS3Client();

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        return response;
    }

}