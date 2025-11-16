FROM maven:3.9.6-eclipse-temurin-22 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine AS runner

WORKDIR /app

COPY --from=builder ./app/target/billing-springapp-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
