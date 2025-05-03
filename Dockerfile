# 1. Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# 2. Указываем автора
LABEL authors="astanserikov"

# 3. Переменные окружения (установим кодировку UTF-8)
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en' LC_ALL='en_US.UTF-8'

# 4. Создадим директорию внутри контейнера для приложения
WORKDIR /app

# 5. Копируем собранный JAR файл в контейнер
COPY build/libs/freeway-0.0.1-SNAPSHOT.jar .

# 6. Укажем, что порт 8080 будет открыт
EXPOSE 8080

# 7. Запуск приложения
ENTRYPOINT ["java", "-jar", "freeway-0.0.1-SNAPSHOT.jar"]
