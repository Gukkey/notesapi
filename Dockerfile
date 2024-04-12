FROM alpine:latest

RUN apk --no-cache add git openjdk17

WORKDIR /app

COPY . .

RUN apk --no-cache add dos2unix

RUN dos2unix ./mvnw

RUN chmod +x ./mvnw

RUN cp ./src/main/resources/application.yml.example ./src/main/resources/application.yml

RUN ./mvnw clean install

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "./target/notesapi-0.0.1-SNAPSHOT.jar"]