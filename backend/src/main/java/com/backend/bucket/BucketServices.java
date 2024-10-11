
package com.backend.bucket;

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

public class BucketServices {
    S3Client s3Client = new com.backend.bucket.S3Provider().getS3Client();
    String bucketName = "vertex-bucket-java";

    public void criarBucket(){
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            System.out.println("Bucket criado com sucesso: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Erro ao criar o bucket: " + e.getMessage());
        }

        try {
            List<Bucket> buckets = s3Client.listBuckets().buckets();
            System.out.println("Lista de buckets:");
            for (Bucket bucket : buckets) {
                System.out.println("- " + bucket.name());
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao listar buckets: " + e.getMessage());
        }
    }

    public void listarBucker(){
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(listObjects).contents();
            System.out.println("Objetos no bucket " + bucketName + ":");
            for (S3Object object : objects) {
                System.out.println("- " + object.key());
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
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

            System.out.println("Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + uniqueFileName);
        } catch (S3Exception e) {
            System.err.println("Erro ao fazer upload do arquivo: " + e.getMessage());
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
                    System.out.println("Arquivo baixado: " + key);
                }
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
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

            System.out.println("Objeto deletado com sucesso: " + objectKeyToDelete);
        } catch (S3Exception e) {
            System.err.println("Erro ao deletar objeto: " + e.getMessage());
        }
    }
}

