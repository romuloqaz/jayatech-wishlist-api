sudo docker pull mongo:latest
./mvnw clean package
sudo docker compose build
sudo docker compose up -d