
package com.backend.bucket;

import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
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
    S3Client s3Client = new com.backend.bucket.S3Provider().getS3Client();
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

    public void enviarArquivo(){
        try {
            String uniqueFileName = UUID.randomUUID().toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();

            File file = new File("arquivoS3.txt");
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            String envioMessage = "Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + uniqueFileName;
            logger.info(envioMessage);
            registrarLog("INFO", envioMessage);
        } catch (S3Exception e) {
            String errorMessage = "Erro ao fazer upload do arquivo: " + e.getMessage();
            logger.error(errorMessage);
            registrarLog("ERROR", errorMessage);
        }
    }

    public void baixarArquivoLocal() {
        try {
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
            for (S3Object object : objects) {
                String key = object.key();

                // Verifica se o arquivo tem a extensão desejada
                if (key.endsWith(".log") || key.endsWith(".xlsx") || key.endsWith(".xls")) {
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                    Files.copy(inputStream, new File(key).toPath());
                    String uploadMessage = "Arquivo baixado: " + key;
                    logger.info(uploadMessage);
                    registrarLog("INFO", uploadMessage);
                }
            }
        } catch (IOException | S3Exception e) {
            String errorMessage = "Erro ao fazer download dos arquivos: " + e.getMessage();
            logger.error(errorMessage);
            registrarLog("ERROR", errorMessage);
        }
    }

    public void deletarArquivo(){
        try {
            String objectKeyToDelete = "identificador-do-arquivo";
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKeyToDelete)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            String dellMessage = "Objeto deletado com sucesso: " + objectKeyToDelete;
            logger.info(dellMessage);
            registrarLog("INFO", dellMessage);
        } catch (S3Exception e) {
            String errorMessage = "Erro ao deletar objeto: " + e.getMessage();
            logger.error(errorMessage);
            registrarLog("ERROR", errorMessage);
        }
    }


    public static void main(String[] args){
        BucketServices bucketServices = new BucketServices();
        logger.info("Iniciando operações no S3.");
        bucketServices.listarBucket();
        bucketServices.enviarArquivo();
        bucketServices.baixarArquivoLocal();
        bucketServices.deletarArquivo();

    }
}