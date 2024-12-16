FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY gradlew gradlew.bat ./
COPY gradle/wrapper/ gradle/wrapper/

RUN chmod +x gradlew

COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon

COPY . .

RUN ./gradlew build --no-daemon -x test || true

RUN ls -l build/libs

EXPOSE 8080

CMD ["java", "-jar", "build/libs/debttrack.platfrom-0.0.1-SNAPSHOT.jar"]