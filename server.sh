sudo docker pull openjdk:17-jdk-slim
sudo docker pull mongo:latest
mvn clean package -DskipTests
sudo docker compose build
sudo docker compose up -d