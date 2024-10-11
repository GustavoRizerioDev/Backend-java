# Projeto de Processamento de Arquivos com Apache POI, AWS S3 e JDBC

Este projeto em Java realiza o processamento de arquivos utilizando a biblioteca Apache POI para manipulação de documentos no formato Excel. Ele busca arquivos armazenados em um bucket da AWS S3, processa os dados e os insere em um banco de dados utilizando JDBC.


## Funcionalidades

- Conexão e download de arquivos do AWS S3.
- Leitura e manipulação de planilhas Excel com Apache POI.
- Inserção e gerenciamento de dados no banco de dados via JDBC.

## Tecnologias Utilizadas

<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,maven,mysql,aws,idea" />
  </a>
</p>

## Requisitos

- **Java 8+:** [Download do JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Maven:** [Download do Maven](https://maven.apache.org/download.cgi)
- **AWS SDK:** (Adicione no aqruivo de dependecias do projeto)
- **Apache POI:**  (Adicione no aqruivo de dependecias do projeto)
- **JDBC Driver:**  (Adicione no aqruivo de dependecias do projeto)
- **Banco de Dados**: [Download do MySQL](https://dev.mysql.com/downloads/mysql/)
- **IDE (opcional, recomendado):**
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
   - [Eclipse](https://www.eclipse.org/downloads/)
   - [NetBeans](https://netbeans.apache.org/download/)
- **Criação de um Bucket na AWS**

## Ambiente

**Configuração do Ambiente**  
Coloque as dependencies necessarias no `pom.xml`

```bash
     <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>6.1.13</version>
        </dependency>

        <dependency>
            <groupId>software.amazon.awssdk</groupId>
            <artifactId>s3</artifactId>
            <version>2.27.21</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.19.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.19.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-dbcp2</artifactId>
            <version>2.12.0</version>
        </dependency>

        <!-- Driver do servidor de banco MySQL -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>9.0.0</version>
        </dependency>

        <!-- Driver do servidor de banco SQLServer -->
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>12.8.1.jre11</version>
        </dependency>

            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi</artifactId>
                <version>5.3.0</version>
            </dependency>
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi-ooxml</artifactId>
                <version>5.3.0</version>
            </dependency>

    </dependencies>


    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <mainClass>school.sptech.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente nas configurações de compilação de projeto do:

`DB_DRIVER_NAME`

`DB_HOST`

`DB_USER`

`DB_PASSWORD`

`AWS_ACCESS_KEY_ID`

`AWS_SECRET_ACCESS_KEY`

`AWS_SESSION_TOKEN`

### Configurações das Variáveis de Ambiente

#### 1 - Abra as configurações de execução:

No topo da janela do IntelliJ IDEA, clique no menu suspenso de configuração de execução (onde você escolhe o modo de execução do projeto).  
Selecione "Edit Configurations...".

![Passo 1](https://i.imgur.com/3oKZ45u.png)

#### 2 - Selecione sua configuração de execução:

Na janela que abrir, selecione a configuração de execução que você deseja editar (geralmente será algo como **Application**, ou terá o nome da classe principal).

![Passo 2](https://i.imgur.com/WMOb4Fm.png)

#### 3 - Adicione variáveis de ambiente:

Com a configuração selecionada, na parte inferior, você verá um campo chamado **"Environment variables"**.  
Clique no ícone ao lado deste campo (parece uma caixa de edição) para abrir a janela de edição de variáveis de ambiente.

![Passo 3](https://i.imgur.com/nh1ShSb.png)

No campo que abrir, você pode definir as variáveis de ambiente no formato `KEY=VALUE`, separadas por ponto e vírgula. Exemplo:

![Exemplo](https://i.imgur.com/8iZXNXI.png)

## Onde achar as variaveis de ambiente da AWS:
##### Inicie um LAB na aws e pegue em detalhes:

<img src="https://i.imgur.com/EscJnJY.png" />

<img src="https://i.imgur.com/0dBQbcu.png" />

## Criando JAR e executando
Dentro do seu pom.xml adicione esta configuração:

```bash
 <build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
                <archive>
                    <manifest>
                        <mainClass>com.example.MainClass</mainClass>
                    </manifest>
                </archive>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Na tag <mainClass> adicione caminho da sua classe até a classe executável.

Vá ao menu do maven ao lado direito da IDE e clique em clean e depois em install:
<img src="https://i.imgur.com/K4JfONg.png" />

Veja se no diretorio target existe dois .jar onde um deve ter o nome "jar-with-dependencies":
<img src="https://i.imgur.com/ZMopagT.png" />

Abra o terminal no diretorio target:

```bash
  cd target
```

Execute o comando:

```bash
  java -jar testeBanco-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Licença
Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.




