FROM openjdk:21-jdk

# Copia el archivo JAR del proyecto Spring Boot al directorio de trabajo del contenedor
COPY ./target/final_laboratorio4-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que se ejecutará la aplicación Spring Boot dentro del contenedor
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot cuando se inicie el contenedor
CMD ["java", "-jar", "app.jar"]
