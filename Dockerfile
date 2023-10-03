FROM openjdk:17-jdk-slim
ADD target/wishlist-api.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]