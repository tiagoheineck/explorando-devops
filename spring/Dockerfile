FROM maven:3.9.8-eclipse-temurin-21-jammy AS builder

WORKDIR /app

COPY . /app

RUN mvn package -DskipTests


FROM eclipse-temurin:21-jdk AS runner

ENV TZ=America/Sao_Paulo
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=America/Sao_Paulo"

COPY --from=builder /app/target/project-0.0.1-SNAPSHOT.jar /project.jar

ENTRYPOINT ["java","-Dhttps.protocols=TLSv1.1,TLSv1.2","-Djavax.net.debug=ssl", "-jar","/project.jar"]

EXPOSE 80
