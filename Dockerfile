FROM amazoncorretto:21.0.0-alpine

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

### To create and start a Docker of this Spring Application, run these in via Terminal in the uppermost project folder
# mvn clean install
# docker build -t sevenprinciples .
# docker run -p 8080:8080 sevenprinciples