FROM openjdk:21-slim

RUN apt-get update && \
    apt-get install -y git && \
    rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/Vertex-PI/Backend-java.git

WORKDIR /Backend-java/backend

CMD ["java", "-Xms512m", "-Xmx10g", "-jar", "testeBanco-1.0-SNAPSHOT-jar-with-dependencies.jar"]