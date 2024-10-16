
package main.java.com.backend.bucket;

import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;


import org.slf4j.Logger;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class BucketServices {
    private static final Logger logger = LoggerFactory.getLogger(BucketServices.class);
    S3Client s3Client = new main.java.com.backend.bucket.S3Provider().getS3Client();
    String bucketName = "vertex-bucket-xls"; //cada nome tem que ser único
    String logName = "backup.log";

    public void criarBucket(){
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            logger.info("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            logger.error("Erro ao criar o bucket: " + e.getMessage());
        }

        try {
            List<Bucket> buckets = s3Client.listBuckets().buckets();
            logger.info("Lista de buckets:");
            for (Bucket bucket : buckets) {
                logger.info("- " + bucket.name());
            }
        } catch (S3Exception e) {
            logger.error("Erro ao listar buckets: " + e.getMessage());
        }
    }

    public void listarBucket(){
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            logger.info("Objetos no bucket " + bucketName + ":");
            for (S3Object object : objects) {
                logger.info("- " + object.key());
            }
        } catch (S3Exception e) {
            logger.error("Erro ao listar objetos no bucket: " + e.getMessage());
        }
    }

    public void enviarAquivo(){
        try {
            String uniqueFileName = UUID.randomUUID().toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();

            File file = new File("arquivoS3.txt");
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            logger.info("Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + uniqueFileName);
        } catch (S3Exception e) {
            logger.error("Erro ao fazer upload do arquivo: " + e.getMessage());
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
                    logger.info("Arquivo baixado: " + key);
                }
            }
        } catch (IOException | S3Exception e) {
            logger.error("Erro ao fazer download dos arquivos: " + e.getMessage());
        }
    }

    public void deletarAquivo(){
        try {
            String objectKeyToDelete = "identificador-do-arquivo";
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKeyToDelete)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);

            logger.info("Objeto deletado com sucesso: " + objectKeyToDelete);
        } catch (S3Exception e) {
            logger.error("Erro ao deletar objeto: " + e.getMessage());
        }
    }

    public void  enviarLogParaS3(){
        try{
            File logFile = new File("backup.log");

            if(!logFile.exists()){
                logger.error("Arquivo de log não encontrado: backup.log");
                return;
            }
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("logs/backup.log")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(logFile));
            logger.info("Arquivo de log enviado com sucesso para S3: logs/{}", logName);
        }catch (Exception e){
            logger.error("Erro ao enviar o arquivo de log para o S3: {}", e.getMessage());
        }
    }

    public static void main(String[] args){
        BucketServices bucketServices = new BucketServices();
        bucketServices.criarBucket();
        bucketServices.listarBucket();
        bucketServices.enviarAquivo();
        bucketServices.baixarArquivoLocal();
        bucketServices.deletarAquivo();

        bucketServices.enviarLogParaS3();
    }
}


