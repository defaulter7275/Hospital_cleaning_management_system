FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/hospital-cleaning.jar app.jar
ENV PORT=8080
CMD ["java", "-jar", "app.jar"]
