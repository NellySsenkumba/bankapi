FROM eclipse-temurin:21.0.2_13-jdk-jammy AS huge
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ./mvnw spring-boot:run


FROM eclipse-temurin:21.0.2_13-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install -Dmaven.test.skip

FROM eclipse-temurin:21.0.2_13-jre-jammy AS final
WORKDIR /app
EXPOSE 7200
COPY --from=builder /app/target/*.jar ./target/*.jar
ENTRYPOINT ["java","-jar","./target/*.jar"]
