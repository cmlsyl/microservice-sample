FROM maven:latest as build

WORKDIR /app

COPY . .

RUN mvn clean install


FROM amazoncorretto:21-alpine

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

CMD ["java", "-jar", "app.jar"]